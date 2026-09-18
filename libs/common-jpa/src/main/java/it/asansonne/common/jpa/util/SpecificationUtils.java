package it.asansonne.common.jpa.util;

import static it.asansonne.common.core.enums.ErrorMessage.FILTER_ERROR;

import it.asansonne.common.core.exception.custom.BadRequestException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

/**
 * The type Specification utils.
 */
@Slf4j
@RequiredArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class SpecificationUtils {

  /**
   * The constant MIN_SEARCH_LENGTH.
   */
  public static final int MIN_SEARCH_LENGTH = 3;

  /**
   * Has text boolean.
   *
   * @param value the value
   * @return the boolean
   */
  public static boolean hasText(String value) {
    return value != null && !value.isBlank();
  }

  /**
   * Gets normalized.
   *
   * @param fieldName the field name
   * @param value     the value
   * @return the normalized
   */
  public static @Nullable String normalizeOrNull(String fieldName, String value) {
    String normalized = value.trim();
    if (normalized.length() < MIN_SEARCH_LENGTH) {
      log.warn(
          "Ignoring filter: field='{}', value='{}', minLettersRequired={}",
          fieldName,
          normalized,
          MIN_SEARCH_LENGTH
      );
      return null;
    }
    return normalized;
  }

  /**
   * Is not deleted specification.
   *
   * @param <T> the type parameter
   * @return the specification
   */
  public static <T> Specification<T> isActive(Boolean isActive) {
    return (root, _, cb) ->
        isActive == null ? null : cb.equal(root.get("isActive"), isActive);
  }

  /**
   * Has uuid specification.
   *
   * @param <T>  the type parameter
   * @param uuid the uuid
   * @return the specification
   */
  public static <T> Specification<T> hasUuid(UUID uuid) {
    return (root, _, cb) ->
        uuid == null ? null : cb.equal(root.get("uuid"), uuid);
  }

  /**
   * Like ignoring short value specification.
   *
   * @param <T>         the type parameter
   * @param entityField the entity field
   * @param filterValue the filter value
   * @return the specification
   */
  public static <T> Specification<T> likeIgnoringShortValue(
      String entityField, String filterValue
  ) {
    return (root, _, cb) -> {
      if (!hasText(filterValue)) {
        return null;
      }
      String normalized = normalizeOrNull(entityField, filterValue);
      if (normalized == null) {
        throw new BadRequestException(FILTER_ERROR.getCode(), MIN_SEARCH_LENGTH);
      }
      return cb.like(
          cb.lower(root.get(entityField)),
          "%" + normalized.toLowerCase() + "%"
      );
    };
  }

}
