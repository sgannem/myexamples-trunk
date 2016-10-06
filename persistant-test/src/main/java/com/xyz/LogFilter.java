package com.xyz;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


/**
 * <p> The LogFilter logs start and end of all servlet requests and outputs the required time and some additional data. </p>
 * <h3>Configuration</h3> System property {@code appstore.node}: name of the cluster node. <p>The LogFilter is used in the web.xml as
 * follows:</p>
 *
 * <pre>
 *     &lt;filter&gt;
 *         &lt;filter-name&gt;LogFilter&lt;/filter-name&gt;
 *         &lt;filter-class&gt;com.nxp.appstore.market.commons.rest.LogFilter&lt;/filter-class&gt;
 *         &lt;init-param&gt;
 *             &lt;param-name&gt;dropurl.0&lt;/param-name&gt;
 *             &lt;param-value&gt;sampleurl_to_ignore&lt;/param-value&gt;
 *         &lt;/init-param&gt;
 *     &lt;/filter&gt;
 *
 *     &lt;filter-mapping&gt;
 *         &lt;filter-name&gt;LogFilter&lt;/filter-name&gt;
 *         &lt;url-pattern&gt;*&lt;/url-pattern&gt;
 *     &lt;/filter-mapping&gt;
 * </pre>
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class LogFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);

  private static final Charset UTF_8 = Charset.forName("UTF-8");
  private static final boolean FORCE_REQUEST_ENCODING = true;
  private static final int MAX_QUERY_STRING_LENGTH = 50;
  private static final double MILLIS_PER_NANO = 1000000.0;

  private static final String MDC_SESSION = "session";

  /**
   * Cluster node id. Important if syslogd is used in order to collect the output of several nodes at a central point.
   */
  private String nodeId = null;

  /**
   * Do not log for the following URLs
   */
  private List<String> dropUrls = null;

  private ThreadMXBean threadBean;


  @Override
  @SuppressWarnings("squid:MethodCyclomaticComplexity")
  public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws
      IOException, ServletException {
    long start = 0L;
    long startCpu = 0L;
    Throwable throwable = null;
    boolean dropped = false;
    String url = "";
    String method = "";
    String agent = null;

    HttpServletRequest httpServletRequest = null;
    HttpServletResponse response = null;

    try {
      Validate.isTrue(servletRequest instanceof HttpServletRequest, "filter oops?");
      httpServletRequest = (HttpServletRequest) servletRequest;
      Validate.isTrue(servletResponse instanceof HttpServletResponse, "filter oops?");
      response = (HttpServletResponse) servletResponse;

      if ((httpServletRequest.getCharacterEncoding() == null) || FORCE_REQUEST_ENCODING) {
        httpServletRequest.setCharacterEncoding(UTF_8.name());
        response.setCharacterEncoding(UTF_8.name());
      }

      url = httpServletRequest.getRequestURI();
      method = httpServletRequest.getMethod();
      agent = httpServletRequest.getHeader("User-Agent");

      dropped = isDroppedUrl(url);

      if (!dropped) {
        setupMdc(httpServletRequest);

        logRequestStart(url, method, agent, getRemoteAddress(httpServletRequest), httpServletRequest.getQueryString());

        if (threadBean != null) {
          startCpu = threadBean.getCurrentThreadCpuTime();
        }

        start = System.nanoTime();
      }

      // continue to handle the request
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (final IOException e) {
      throwable = e;
      throw e;
    } catch (final ServletException e) {
      if (e.getRootCause() == null) {
        throwable = e;
      } else {
        throwable = e.getRootCause();
      }
      throw e;
    } catch (@java.lang.SuppressWarnings("squid:S1181") final Throwable e) { // be sure to get all errors
      throwable = e;
      throw new ServletException(e);
    } finally {
      if (!dropped) {
        logRequest(start, startCpu, throwable, url, method, agent, httpServletRequest, response);
      }

      removeMDC();
    }
  }

  @SuppressWarnings("squid:S00107")
  private void logRequest(final long start, final long startCpu, final Throwable throwable, final String url, final String method, final
  String agent, final HttpServletRequest httpServletRequest, final HttpServletResponse response) {
    long cpuTime = 0L;

    if (threadBean != null) {
      cpuTime = threadBean.getCurrentThreadCpuTime() - startCpu;
    }

    final long time = System.nanoTime() - start;
    final int statusCode = determineStatusCode(response, throwable);
    final StringBuilder msg = new StringBuilder(100);

    msg.append("request done ").append(method).append(' ').append(url).append(" code=").append(statusCode).append(" time=").append
        (formatNanos(time)).append("ms");

    if (threadBean != null) {
      msg.append(" cpu=").append(formatNanos(cpuTime)).append("ms");
    }

    if (throwable == null) {
      logWithoutThrowable(statusCode, msg);
    } else {
      logWithThrowable(throwable, agent, httpServletRequest, msg);
    }
  }

  private void logWithoutThrowable(final int statusCode, final StringBuilder msg) {
    if (statusCode >= HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
      // switch to error in case of code 500 or higher
      LOGGER.error(msg.toString());
    } else {
      LOGGER.info(msg.toString());
    }
  }

  private void logWithThrowable(final Throwable throwable, final String agent, final HttpServletRequest httpServletRequest, final
  StringBuilder msg) {
    msg.append(" ex=").append(throwable.getClass().getSimpleName());
    msg.append(" msg=").append(throwable.getMessage());
    msg.append(" UA=").append(agent); // also log agent in error/warning case

    LOGGER.error(msg.toString());

    if (httpServletRequest != null) {
      // log all (post) parameters in case of warn/error
      final Enumeration<String> params = httpServletRequest.getParameterNames();
      final StringBuilder paramsStr = new StringBuilder();

      while (params.hasMoreElements()) {
        final String name = params.nextElement();
        paramsStr.append(name).append("=").append(httpServletRequest.getParameter(name)).append(" ");
      }

      LOGGER.info("params: {}", paramsStr);
    }
  }

  private void logRequestStart(final String url, final String method, final String agent, final String remoteAddress, final String
      queryString) {
    final String printableQueryString = (queryString == null) ? "" : ("?" + StringUtils.abbreviate(queryString, MAX_QUERY_STRING_LENGTH));

    LOGGER.info("request start {} {}{} remote address:{} UA={}", method, url, printableQueryString, remoteAddress, agent);
  }

  private int determineStatusCode(final HttpServletResponse response, final Throwable throwable) {
    if (response == null) {
      return -1;
    }

    final int responseStatus = response.getStatus();

    if (throwable == null) {
      return responseStatus;
    }

    if (responseStatus == HttpServletResponse.SC_OK) {
      // If (throwable != null) but  (responseStatus == SC_OK), probably an exception occurred in the Jersey layer.
      // The layer will return HTTP error code 500, which is the hardcoded error code in org.apache.catalina.core.StandardWrapperValve
      // .exception().
      return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    return responseStatus;
  }

  private boolean isDroppedUrl(final String url) {
    // does any stopUrl match url
    for (final String stopUrl : dropUrls) {
      if (url.contains(stopUrl)) {
        return true;
      }
    }

    return false;
  }

  private void setupMdc(final HttpServletRequest httpServletRequest) {
    final HttpSession session = httpServletRequest.getSession(false);
    String sessionString = "";

    if (session != null) {
      final String sessionId = session.getId();
      final Principal userPrincipal = httpServletRequest.getUserPrincipal();

      if (userPrincipal == null) {
        sessionString = "(sessionId=" + sessionId + ") ";
      } else {
        final String username = userPrincipal.getName();
        sessionString = "(sessionId=" + sessionId + ",username=" + username + ") ";
      }
    }

    MDC.put(MDC_SESSION, sessionString);
  }

  private void removeMDC() {
    MDC.remove(MDC_SESSION);
  }

  private String formatNanos(final long nanos) {
    // show millis with 3 digits (i.e. us)
    return new DecimalFormat("#.###", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(nanos / MILLIS_PER_NANO);
  }

  private String getRemoteAddress(final HttpServletRequest servletRequest) {
    final String forwarded = servletRequest.getHeader("X-Forwarded-For");

    if (forwarded == null) {
      return servletRequest.getRemoteAddr();
    } else {
      return forwarded;
    }
  }

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    nodeId = System.getProperty("appstore.node");

    if (nodeId == null) {
      try {
        nodeId = InetAddress.getLocalHost().getHostName();

        if (nodeId.contains(".")) {
          // strip domain if any (e.g. abcd.nxp.com -> abcd)
          nodeId = nodeId.split("\\.")[0];
        }
      } catch (final Exception e) {
        LOGGER.warn("failed to read host name.", e);
      }
    }

    dropUrls = new ArrayList<String>();
    int i = 0;
    String dropUrlParam;

    while ((dropUrlParam = filterConfig.getInitParameter("dropurl." + i)) != null) {
      LOGGER.info("Adding dropurl.{} {}", i, dropUrlParam);
      dropUrls.add(dropUrlParam);
      i++;
    }

    threadBean = ManagementFactory.getThreadMXBean();
  }


  @Override
  public void destroy() {
    // nothing to do
  }
}
