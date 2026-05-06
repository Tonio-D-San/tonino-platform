package it.asansonne.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Error message.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
  // Security / Authentication / Authorization
  UNAUTHORIZED_ACCESS("error.common.auth.authentication.failed"),
  FORBIDDEN("error.common.forbidden.access"),
  JWT_ERROR("error.common.auth.invalid.token"),

  // Validation / Bad request
  BAD_REQUEST("error.common.bad.request"),
  FILTER_ERROR("error.common.filter"),

  // Not found
  DTO_NOT_FOUND("error.common.dto.not.found"),
  MODEL_NOT_FOUND("error.common.model.not.found"),

  // Technical / System
  UNCAUGHT_ERROR("error.common.uncaught"),
  NOT_IMPLEMENTED("error.common.method.not.allowed");

  private final String code;

}
