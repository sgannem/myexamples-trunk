package com.xyz;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Abstract base class for all entity classes.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@MappedSuperclass
public abstract class AbstractEntity implements Entity {

  private long version;

  protected AbstractEntity() {
    // empty
  }

  AbstractEntity(final long version) {
    this.version = version;
  }

  @Version
  public long getVersion() {
    return version;
  }

  public void setVersion(final long version) {
    this.version = version;
  }
}
