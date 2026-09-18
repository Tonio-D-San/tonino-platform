package it.asansonne.rest.jpa.peopleservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Error message.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
  // Not found
  USER_NOT_FOUND("error.people.user.not.found"),
  GROUP_NOT_FOUND("error.people.group.not.found"),

  // Duplicate
  EMAIL_DUPLICATE("error.people.email.duplicate"),
  FISCAL_CODE_DUPLICATE("error.people.fiscalcode.duplicate");

  private final String code;

}
