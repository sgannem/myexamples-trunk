package com.xyz;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Puts the id of the logged-in {@link Administrator} into the session context.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Singleton
public class AdminIdProviderFilter extends AbstractIdProviderFilter<Administrator> {

  @Inject
  public AdminIdProviderFilter(final AdministratorRepository administratorRepository) {
    super(administratorRepository);
    System.out.println("###AdminIdProviderFilter is initialised...");
  }
}
