package com.xyz;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;

/**
 * An administrator.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@javax.persistence.Entity
public class Administrator extends AbstractStrongEntity {

  @Embedded
  private User user;
  private String email;

  private Administrator(final Long id, final User user, final String email) {
    super(id);
    this.user = user;
    this.email = email;
  }

  Administrator() {
    // empty
  }

  public static Administrator newInstance(final long id, final User user, final String email) {
    return new Administrator(id, user, email);
  }

  public User getUser() {
    return user;
  }

  void setUser(final User user) {
    this.user = user;
  }

  @Column
  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final Administrator that = (Administrator) o;
    return Objects.equals(getUser(), that.getUser());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUser());
  }

  @Override
  public String toString() {
    return "Administrator{" +
        "user=" + user +
        ", email='" + email + '\'' +
        '}';
  }
}
