package com.xyz;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Utility methods.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class WebUtils {

  private WebUtils() {
    // empty
  }

  public static void writeJsonFieldIfNotNull(final JsonGenerator gen, final String fieldName, final String value) throws IOException {
    if (value != null) {
      gen.writeStringField(fieldName, value);
    }
  }

  public static void writeJsonFieldIfNotNull(final JsonGenerator gen, final String fieldName, final Integer value) throws IOException {
    if (value != null) {
      gen.writeNumberField(fieldName, value);
    }
  }

  public static void writeJsonFieldIfNotNull(final JsonGenerator gen, final String fieldName, final byte[] value) throws IOException {
    if (value != null) {
      gen.writeBinaryField(fieldName, value);
    }
  }
}
