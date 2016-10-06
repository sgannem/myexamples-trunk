package com.xyz;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A database query within the domain.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */

public interface DomainQuery<T> {

  /**
   * Execute query and returns the result list or empty list
   *
   * @return list of query results
   */
  List<T> getResultList();

  /**
   * Execute query and applies the given function to the result list.
   *
   * @return list of query results
   */
  <S> List<S> getResultList(Function<? super T, S> mapper);

  /**
   * Execute query and returns a single result.
   *
   * @return result
   */
  Optional<T> getSingleResult();

  /**
   * Execute query and returns a single result or throws an {@link EntityNotFoundInRepoException}
   *
   * @return result
   */
  T getSingleResultOrThrow() throws EntityNotFoundInRepoException;

  /**
   * Execute query and returns a single result or throws Exception {@link E} that is returned by the given {@link Supplier}.
   *
   * @return result or null
   */
  <E extends Throwable> T getSingleResultOrThrow(Supplier<? extends E> exceptionSupplier) throws E;

  /**
   * Return count of selected rows
   *
   * @return count
   */
  int getResultSize();

  /**
   * Set the position of the first result to retrieve.
   *
   * @param firstResult index of first result
   */
  void setFirstResult(int firstResult);

  /**
   * Set the maximum number of results to retrieve.
   *
   * @param maxResults number of results
   */
  void setMaxResults(int maxResults);

  /**
   * Add ascending sort criteria to the query
   *
   * @param propertyName name of the property
   */
  void addAscSortedBy(String propertyName);

  /**
   * Add descending sort criteria to the query
   *
   * @param propertyName name of the property
   */
  void addDescSortedBy(String propertyName);

  /**
   * Finds an entity by id.
   *
   * @param id id of the entity
   * @return the entity
   */
  Optional<T> findById(long id);

  /**
   * Finds an entity by id or throws a {@link EntityNotFoundInRepoException}.
   *
   * @param id id of the entity
   * @return the entity
   * @throws EntityNotFoundInRepoException if no entity with the given id exists
   */
  T findByIdOrThrow(long id) throws EntityNotFoundInRepoException;
}
