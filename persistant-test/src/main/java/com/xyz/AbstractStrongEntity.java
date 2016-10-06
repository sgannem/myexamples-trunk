package com.xyz;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

/**
 * An entity with a unique id field.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@MappedSuperclass
public abstract class AbstractStrongEntity extends AbstractEntity {

  public static final int TABLE_GENERATOR_ALLOCATION_SIZE = 50;

  private Long id;

  protected AbstractStrongEntity() {
    super();
  }

  AbstractStrongEntity(final Long id, final long version) {
    super(version);
    this.id = id;
  }

  AbstractStrongEntity(final Long id) {
    this();
    this.id = id;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "SEQUENCE_STORE")
  @TableGenerator(name = "SEQUENCE_STORE", allocationSize = TABLE_GENERATOR_ALLOCATION_SIZE)
  public Long getId() {
    return id;
  }

  void setId(final long id) {
    this.id = id;
  }
}
