package it.asansonne.peopleservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Error message.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
  // Not found
  USER_NOT_FOUND("error.adms.user.not.found"),
  GROUP_NOT_FOUND("error.adms.group.not.found"),
  URL_NOT_FOUND("error.adms.url.not.found"),

  // Conflict / Duplicate / Data integrity
  CONFLICT_ERROR("error.adms.conflict"),
  EMAIL_DUPLICATE("error.adms.email.duplicate"),
  FISCAL_CODE_DUPLICATE("error.adms.fiscalcode.duplicate"),
  DATA_INTEGRITY("error.adms.data.integrity"),

  // External services
  KEYCLOAK_CALL_ERROR("error.adms.keycloak.call"),

  // Technical / System
  NULL_HTTP_STATUS_CODE("error.adms.null.http.status.code");

  private final String code;

}
