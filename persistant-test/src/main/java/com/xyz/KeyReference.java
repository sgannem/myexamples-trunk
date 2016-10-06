package com.xyz;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 * Stores key values.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@javax.persistence.Entity
public class KeyReference implements Entity {

  private Long id;
  private KeyTypeEnum keyType;
  private byte[] key;

  private KeyReference(@Nullable final Long id, @Nullable final KeyTypeEnum keyType, @Nullable final byte[] key) {
    this.id = id;
    this.keyType = keyType;
    this.key = key;
  }

  private KeyReference() {
    this(null, null, null);
  }

  public static KeyReference newInstance(final KeyTypeEnum keyType, final byte[] key) {
    return new KeyReference(null, keyType, key);
  }

  public static KeyReference newTestInstance(final long id, final KeyTypeEnum keyType, final byte[] key) {
    return new KeyReference(id, keyType, key);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "SEQUENCE_STORE")
  @TableGenerator(name = "SEQUENCE_STORE", allocationSize = 50)
  public Long getId() {
    return id;
  }

  void setId(final Long id) {
    this.id = id;
  }

  @Enumerated(EnumType.STRING)
  public KeyTypeEnum getKeyType() {
    return keyType;
  }

  void setKeyType(final KeyTypeEnum keyType) {
    this.keyType = keyType;
  }

  @Column
  public byte[] getKey() {
    return key;
  }

  void setKey(final byte[] key) {
    this.key = key;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final KeyReference that = (KeyReference) o;

    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "KeyReference{" +
        "id=" + id +
        ", keyType=" + keyType +
        '}';
  }
}
