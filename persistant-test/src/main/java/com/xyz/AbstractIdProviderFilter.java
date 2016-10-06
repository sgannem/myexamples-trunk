package com.xyz;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Puts the id of the logged-in user into the session context.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public abstract class AbstractIdProviderFilter<T extends AbstractStrongEntity> implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIdProviderFilter.class);

  private final UsernameRepository<T> usernameRepository;

  protected AbstractIdProviderFilter(final UsernameRepository idRepository) {
    this.usernameRepository = idRepository;
  }

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    // empty
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
      ServletException {
    final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    final Principal principal = httpServletRequest.getUserPrincipal();
    final HttpSession session = httpServletRequest.getSession(false);

    if ((principal != null) && isAttributeNotSet(session, "userid")) {
      final String username = principal.getName();
      final Optional<T> user = usernameRepository.getByUsername(username);
      final Long id = user.orElseThrow(RuntimeException::new).getId();
      ((HttpServletRequest) request).getSession().setAttribute("userid", id);
      LOGGER.info("username is {}, user id is {}", username, id);
    }

    chain.doFilter(request, response);
  }

  private boolean isAttributeNotSet(final HttpSession session, final String attribute) {
    return (session == null) || (session.getAttribute(attribute) == null);
  }

  @Override
  public void destroy() {
    // empty
  }
}

