package com.xyz;

import java.util.Optional;

/**
 * Provides a lookup by username.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public interface UsernameRepository<T extends AbstractStrongEntity> {

  Optional<T> getByUsername(String username);

  boolean existsWithUsername(String username);
}
