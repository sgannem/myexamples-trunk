package com.xyz;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Registers {@link JsonSerializer} that map entity classes to json.
 *
 * @author Martin Reiterer (martin.reiterer@rise-world.com)
 */
public class JacksonModule extends SimpleModule {

  public JacksonModule() {
    super(JacksonModule.class.getSimpleName(), new Version(1, 0, 0, null, null, null));

    addSerializer(new ApplicationProviderSerializer());
  }
}
