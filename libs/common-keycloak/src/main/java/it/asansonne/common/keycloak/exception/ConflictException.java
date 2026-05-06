package it.asansonne.common.keycloak.exception;

import lombok.Getter;

/**
 * The type Inactive user exception.
 */
@Getter
public class ConflictException extends RuntimeException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Conflict exception.
   *
   * @param errorCode the message
   * @param args      the args
   */
  public ConflictException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}
