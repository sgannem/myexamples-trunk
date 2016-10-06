package com.xyz;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * User is an embedded Object for both, {@link Application} and the {@link com.nxp.appstore.entity.CardIssuer}. This objects contains the
 * necessary fields for login name and working with passwords.
 *
 * @author Gerald Madlmayr (gerald.madlmayr@rise-world.com)
 */
@Embeddable
public class User {

  @Column(length = 50, nullable = false)
  private String username;

  @Column(nullable = false)
  private byte[] passwordhash;

  @Column(nullable = false)
  private byte[] salt;

  @Column(nullable = false)
  private Integer iterations;

  private User(@Nullable final String username, @Nullable final byte[] passwordhash, @Nullable final byte[] salt, @Nullable final Integer
      iterations) {
    this.username = username;
    this.passwordhash = passwordhash;
    this.salt = salt;
    this.iterations = iterations;
  }

  User() {
    this(null, null, null, null);
  }

  public static User newInstance(@Nullable final String username, @Nullable final byte[] passwordhash, @Nullable final byte[] salt, final
  Integer iterations) {
    return new User(username, passwordhash, salt, iterations);
  }

  public static User newInstance() {
    return new User(null, null, null, 0);
  }

  public String getUsername() {
    return username;
  }

  void setUsername(final String username) {
    this.username = username;
  }

  public byte[] getPasswordhash() {
    return passwordhash;
  }

  public void setPasswordhash(final byte[] passwordhash) {
    this.passwordhash = passwordhash;
  }

  public byte[] getSalt() {
    return salt;
  }

  public void setSalt(final byte[] salt) {
    this.salt = salt;
  }

  public Integer getIterations() {
    return iterations;
  }

  public void setIterations(final Integer iterations) {
    this.iterations = iterations;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final User user = (User) o;
    return Objects.equals(getUsername(), user.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsername());
  }

  @Override
  public String toString() {
    return "User{" +
        "username='" + username + '\'' +
        '}';
  }
}

