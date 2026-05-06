package it.asansonne.common.keycloak.exception;

import lombok.Getter;

/**
 * The type Duplicate field exception.
 */
@Getter
public class DuplicateFieldException extends RuntimeException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Duplicate field exception.
   *
   * @param errorCode the errorCode
   * @param args    the args
   */
  public DuplicateFieldException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}
