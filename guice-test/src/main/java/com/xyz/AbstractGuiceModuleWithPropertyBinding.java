package com.xyz;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Binds the properties in the given property files.
 *
 * The property values are accessed like this:
 *
 * {@code @Named("propName") String prop}
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public abstract class AbstractGuiceModuleWithPropertyBinding extends AbstractModule {

  private static final String[] PROPERTY_FILES = {"version.properties"};

  @Override
  protected void configure() {
    for (final String propertyFile : PROPERTY_FILES) {
      final URL resource = getClass().getResource("/" + propertyFile);
      System.out.println("##configuring version.properties....");
      if (resource != null) {
        try (final InputStream propertiesFileStream = resource.openStream()) {
          final Properties properties = new Properties();
          properties.load(propertiesFileStream);
          Names.bindProperties(binder(), properties);
        } catch (final IOException e) {
          throw new IllegalStateException(e);
        }
      }
    }
  }
}
