package com.xyz;

/**
 * An entity.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public interface Entity {

  @Override
  boolean equals(Object object);

  @Override
  int hashCode();
}
