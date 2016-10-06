package com.xyz;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

/**
 * Replacement for Guice's {@code PersistFilter}. This filter suppresses the {@link IllegalStateException} that is thrown when the {@code
 * start()} method of {@link PersistService} is called more than one time.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Singleton
public class SilentPersistFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SilentPersistFilter.class);

  private final PersistFilter persistFilter;

  @Inject
  public SilentPersistFilter(final UnitOfWork unitOfWork, final PersistService persistService) {
    this.persistFilter = new PersistFilter(unitOfWork, persistService);
  }

//  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    try {
      persistFilter.init(filterConfig);
    } catch (final IllegalStateException e) {
      LOGGER.info("Hide exception: {}", e.getMessage());
    }
  }

//  @Override
  public void destroy() {
    persistFilter.destroy();
  }

//  @Override
  public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws
      IOException, ServletException {
    persistFilter.doFilter(servletRequest, servletResponse, filterChain);
  }
}
