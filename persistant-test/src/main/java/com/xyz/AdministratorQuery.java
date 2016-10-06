package com.xyz;

import javax.persistence.EntityManager;

/**
 * {@link DomainQuery} for retrieving {@link Administrator}s.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public class AdministratorQuery extends AbstractDomainQuery<Administrator> implements DomainQuery<Administrator> {

  private AdministratorQuery(final EntityManager entityManager) {
    super(Administrator.class, entityManager, "a");
  }

  public static AdministratorQuery newInstance(final EntityManager entityManager) {
    return new AdministratorQuery(entityManager);
  }

  public AdministratorQuery withUsername(final String username) {
    addCondition("a.user.username=:username", "username", username);
    return this;
  }
}
