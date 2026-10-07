package it.asansonne.rest.jpa.identityservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Error message.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
  // Not found
  USER_NOT_FOUND("error.identity.user.not.found"),
  GROUP_NOT_FOUND("error.identity.group.not.found"),

  // Duplicate
  EMAIL_DUPLICATE("error.identity.email.duplicate"),
  FISCAL_CODE_DUPLICATE("error.identity.fiscalcode.duplicate"),
  GROUP_ROLE_DUPLICATE("error.identity.group.role.duplicate"),
  USER_PHONE_REQUIRED("error.identity.user.phone.required"),
  USER_DATA_INTEGRITY("error.identity.user.data.integrity"),
  GROUP_DATA_INTEGRITY("error.identity.group.data.integrity"),

  // Keycloak
  KEYCLOAK_DELETE_USER_ERROR("error.identity.keycloak.delete.user");

  private final String code;

}
