package it.asansonne.common.keycloak.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Error message.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
  // Not found
  URL_NOT_FOUND("error.adms.url.not.found"),

  // Conflict / Duplicate / Data integrity
  CONFLICT_ERROR("error.adms.conflict"),

  // External services
  KEYCLOAK_CALL_ERROR("error.adms.keycloak.call"),

  // Technical / System
  NULL_HTTP_STATUS_CODE("error.adms.null.http.status.code");

  private final String code;

}
