package com.xyz;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Singleton
public class AdministratorDao implements AdministratorRepository {

  private final DomainQueryFactory domainQueryFactory;

  @Inject
  public AdministratorDao(final DomainQueryFactory domainQueryFactory) {
    this.domainQueryFactory = domainQueryFactory;
  }

  @Override
  public List<Administrator> getAll() {
    final AdministratorQuery administratorQuery = domainQueryFactory.newAdministratorQuery();
    administratorQuery.addAscSortedBy("user.username");
    return administratorQuery.getResultList();
  }

  @Override
  public Optional<Administrator> getByUsername(final String username) {
    final AdministratorQuery administratorQuery = domainQueryFactory.newAdministratorQuery();
    administratorQuery.withUsername(username);
    return administratorQuery.getSingleResult();
  }

  @Override
  public boolean existsWithUsername(final String username) {
    throw new IllegalStateException("not implemented");
  }
}
