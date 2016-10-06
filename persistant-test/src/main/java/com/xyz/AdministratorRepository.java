package com.xyz;

import java.util.List;

/**
 * Repository of {@link Administrator}s.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public interface AdministratorRepository extends UsernameRepository<Administrator> {

  List<Administrator> getAll();
}
