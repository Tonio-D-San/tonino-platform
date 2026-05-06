package it.asansonne.common.keycloak.exception;

import lombok.Getter;

/**
 * The type Argument exception.
 */
@Getter
public class KeycloakCallException extends IllegalArgumentException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Not found exception.
   *
   * @param errorCode the message
   * @param args      the args
   */
  public KeycloakCallException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}