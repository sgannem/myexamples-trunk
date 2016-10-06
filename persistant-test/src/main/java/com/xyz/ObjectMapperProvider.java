package com.xyz;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * Provides an instance of {@code ObjectMapper} that is used by Jersey for JSON serialization.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Provider
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

  private final ObjectMapper objectMapper;

  @Inject
  public ObjectMapperProvider(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public ObjectMapper getContext(final Class<?> type) {
    return objectMapper;
  }
}
