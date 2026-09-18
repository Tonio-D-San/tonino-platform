package it.asansonne.common.keycloak.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Error message.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
  // External services
  KEYCLOAK_CALL_ERROR("error.common.kc.call");

  private final String code;

}
