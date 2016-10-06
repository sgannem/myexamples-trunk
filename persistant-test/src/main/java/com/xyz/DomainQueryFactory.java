package com.xyz;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Factory for {@link DomainQuery}s.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Singleton
public class DomainQueryFactory {

  private final Provider<EntityManager> entityManagerProvider;

  @Inject
  DomainQueryFactory(final Provider<EntityManager> entityManagerProvider) {
    this.entityManagerProvider = entityManagerProvider;
  }

  public static DomainQueryFactory newInstance(final Provider<EntityManager> entityManagerProvider) {
    return new DomainQueryFactory(entityManagerProvider);
  }

  public Provider<EntityManager> getEntityManagerProvider() {
    return entityManagerProvider;
  }

  public AdministratorQuery newAdministratorQuery() {
    return AdministratorQuery.newInstance(entityManagerProvider.get());
  }

  
}
