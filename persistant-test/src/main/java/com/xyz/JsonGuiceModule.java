package com.xyz;

import javax.ws.rs.ext.MessageBodyWriter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.guice.ObjectMapperModule;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Define bindings for Jersey and Jackson.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class JsonGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new ObjectMapperModule().in(Singleton.class));
    bind(ObjectMapperProvider.class);
    bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);
  }
}
