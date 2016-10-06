package com.xyz;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link DomainQuery}, providing many helper methods to build and execute queries.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public abstract class AbstractDomainQuery<T> implements DomainQuery<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDomainQuery.class);

  private Map<String, Object> parameterMap = new HashMap<>();
  private StringBuilder conditions = new StringBuilder();
  private List<String> orderBy = new ArrayList<>();
  private List<String> groupBy = new ArrayList<>();
  private String defaultOrderBy;
  private Integer maxResults;
  private Integer firstResult;

  private final Class<T> mainClass;
  private final EntityManager entityManager;
  private final String entityPrefix;
  private String baseQuery;
  private String countQuery;

  protected AbstractDomainQuery(final Class<T> mainClass, final EntityManager entityManager, final String entityPrefix, @Nullable final
  String baseQuery) {
    this.mainClass = requireNonNull(mainClass);
    this.entityManager = requireNonNull(entityManager);
    this.entityPrefix = requireNonNull(entityPrefix);
    this.baseQuery = baseQuery;
  }

  protected AbstractDomainQuery(final Class<T> mainClass, final EntityManager entityManager, final String entityPrefix, @Nullable final
  String baseQuery, final String countQuery) {
    this.mainClass = requireNonNull(mainClass);
    this.entityManager = requireNonNull(entityManager);
    this.entityPrefix = requireNonNull(entityPrefix);
    this.baseQuery = baseQuery;
    this.countQuery = countQuery;
  }

  protected AbstractDomainQuery(final Class<T> mainClass, final EntityManager entityManager, final String entityPrefix) {
    this(mainClass, entityManager, entityPrefix, "FROM " + mainClass.getSimpleName() + " " + entityPrefix, "SELECT COUNT(" + entityPrefix
        + ") FROM " + mainClass.getSimpleName() + " " + entityPrefix);
  }

  protected void addCondition(final String condition) {
    conditions.append(" AND ").append(condition);
  }

  protected void addCondition(final String condition, final String parameterName, final Object parameterValue) {
    addCondition(condition);
    addParameter(parameterName, parameterValue);
  }

  protected void addParameter(final String name, final Object value) {
    parameterMap.put(name, value);
  }

  protected void addAscOrderBy(final String name) {
    orderBy.add(name + " ASC");
  }

  protected void addDescOrderBy(final String name) {
    orderBy.add(name + " DESC");
  }

  protected String getDefaultOrderBy() {
    return defaultOrderBy;
  }

  protected void setDefaultOrderBy(final String defaultOrderBy) {
    this.defaultOrderBy = defaultOrderBy;
  }

  protected void addGroupBy(final String name) {
    groupBy.add(name);
  }

  protected void setBaseQuery(final String baseQuery) {
    this.baseQuery = baseQuery;
  }

  @Override
  public List<T> getResultList() {
    final TypedQuery<T> query = createQuery(mainClass, false);
    return query.getResultList();
  }

  @Override
  public <S> List<S> getResultList(final Function<? super T, S> mapper) {
    final List<T> resultList = getResultList();
    return resultList.stream().map(mapper).collect(Collectors.toList());
  }

  protected <S> List<T> getResultList(final Class<S> clazz, final Function<S, T> mapper) {
    final TypedQuery<S> query = createQuery(clazz, false);
    final List<S> resultList = query.getResultList();
    return resultList.stream().map(mapper).collect(Collectors.toList());
  }

  @Override
  public Optional<T> getSingleResult() {
    final TypedQuery<T> query = createQuery(mainClass, false);
    final List<T> result = query.getResultList();

    if (result.isEmpty()) {
      return Optional.empty();
    }

    if (result.size() == 1) {
      return Optional.of(result.get(0));
    }

    throw new IllegalStateException("inconsistent DB. query returns more than one row: " + query.toString());
  }

  @Override
  public T getSingleResultOrThrow() throws EntityNotFoundInRepoException {
    return getSingleResult().orElseThrow(() -> EntityNotFoundInRepoException.newInstance(mainClass));
  }

  @Override
  public <E extends Throwable> T getSingleResultOrThrow(final Supplier<? extends E> exceptionSupplier) throws E {
    return getSingleResult().orElseThrow(exceptionSupplier);
  }

  @Override
  public int getResultSize() {
    final Number resultSize = createQuery(Long.class, true).getSingleResult();
    return (resultSize == null) ? 0 : resultSize.intValue();
  }

  protected void beforeCreateQuery() {
    // empty
  }

  protected String buildStatement(final String base, final boolean count) {
    final StringBuilder stmt = new StringBuilder(base).append(" WHERE 1 = 1 ").append(conditions);

    if (!groupBy.isEmpty()) {
      stmt.append(" GROUP BY ").append(StringUtils.join(groupBy, ","));
    }

    if (!count) {
      if (!orderBy.isEmpty()) {
        stmt.append(" ORDER BY ").append(StringUtils.join(orderBy, ','));
      } else if (!StringUtils.isEmpty(defaultOrderBy)) {
        stmt.append(" ORDER BY ").append(defaultOrderBy);
      }
    }

    return stmt.toString();
  }

  protected <S> TypedQuery<S> createQuery(final Class<S> clazz, final boolean count) {
    final Map<String, Object> oldParameterMap = parameterMap;
    final StringBuilder oldConditions = conditions;
    final List<String> oldOrderBy = orderBy;
    final List<String> oldGroupBy = groupBy;
    parameterMap = new HashMap<String, Object>(oldParameterMap);
    conditions = new StringBuilder(oldConditions);
    orderBy = new ArrayList<String>(oldOrderBy);
    groupBy = new ArrayList<String>(oldGroupBy);
    beforeCreateQuery();

    String queryString;

    if (count) {
      if (isNull(this.countQuery)) {
        throw new IllegalStateException("query object does not specify a count query");
      } else {
        queryString = this.countQuery;
      }
    } else {
      queryString = this.baseQuery;
    }

    final String queryAsString = buildStatement(queryString, count);

    LOGGER.debug("query={}", queryAsString);

    final TypedQuery<S> query = entityManager.createQuery(queryAsString, clazz);

    for (final Map.Entry<String, Object> entry : parameterMap.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }

    parameterMap = oldParameterMap;
    conditions = oldConditions;
    orderBy = oldOrderBy;
    groupBy = oldGroupBy;

    if (!count) {
      if (firstResult != null) {
        query.setFirstResult(firstResult);
      }

      if (maxResults != null) {
        query.setMaxResults(maxResults);
      }
    }

    return query;
  }

  @Override
  public void addAscSortedBy(final String propertyName) {
    addAscOrderBy(entityPrefix + "." + propertyName);
  }

  @Override
  public void addDescSortedBy(final String propertyName) {
    addDescOrderBy(entityPrefix + "." + propertyName);
  }

  @Override
  public void setFirstResult(final int firstResult) {
    this.firstResult = firstResult;
  }

  @Override
  public void setMaxResults(final int maxResults) {
    this.maxResults = maxResults;
  }

  @Override
  public Optional<T> findById(final long id) {
    return Optional.ofNullable(entityManager.find(mainClass, id));
  }

  @Override
  public T findByIdOrThrow(final long id) throws EntityNotFoundInRepoException {
    return findById(id).orElseThrow(() -> EntityNotFoundInRepoException.newInstance(mainClass, id));
  }

  @SuppressWarnings("unchecked")
  protected static <T> T getColumn(final Object row, final int columnIndex) {
    return (T) ((Object[]) row)[columnIndex];
  }
}
