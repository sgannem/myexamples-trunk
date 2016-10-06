package com.xyz;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Thrown if an entity can not be found in the repository/database.
 *
 * @author Gerald Madlmayr (gerald.madlmayr@rise-world.com)
 */
public class EntityNotFoundInRepoException extends Exception {

  private static final String ERROR_MSG_NO_IDS = "No Entity '%s' with the given criteria found";
  private static final String ERROR_MSG_SINGLE_ID = "Entity '%s' with id '%d' not found in database.";
  private static final String ERROR_MSG_MULTIPLE_IDS = "Entity '%s' with ids ('%s') not found in database.";
  private static final String ID_SEPARATOR = "/";

  private EntityNotFoundInRepoException(final String message) {
    super(message);
  }

  public static EntityNotFoundInRepoException newInstance(final String message) {
    return new EntityNotFoundInRepoException(message);
  }

  public static EntityNotFoundInRepoException newInstance(final Class<?> clazz) {
    return new EntityNotFoundInRepoException(String.format(ERROR_MSG_NO_IDS, clazz.getSimpleName()));
  }

  public static EntityNotFoundInRepoException newInstance(final Class<?> clazz, final long... ids) {
    return new EntityNotFoundInRepoException(buildMessage(clazz, ids));
  }

  public static EntityNotFoundInRepoException newInstance(final Class<?> clazz, final String... ids) {
    return new EntityNotFoundInRepoException(buildMessage(clazz, ids));
  }

  private static String buildMessage(final Class<?> clazz, final long... ids) {
    if (ids.length == 0) {
      throw new IllegalArgumentException();
    }

    if (ids.length == 1) {
      return String.format(ERROR_MSG_SINGLE_ID, clazz.getSimpleName(), ids[0]);
    }

    final String idsAsString = Arrays.stream(ids).mapToObj(String::valueOf).collect(Collectors.joining(ID_SEPARATOR));
    return String.format(ERROR_MSG_MULTIPLE_IDS, clazz.getSimpleName(), idsAsString);
  }

  private static String buildMessage(final Class<?> clazz, final String... ids) {
    final String idsAsString = Arrays.stream(ids).map(String::valueOf).collect(Collectors.joining(ID_SEPARATOR));
    return String.format(ERROR_MSG_MULTIPLE_IDS, clazz.getSimpleName(), idsAsString);
  }
}
