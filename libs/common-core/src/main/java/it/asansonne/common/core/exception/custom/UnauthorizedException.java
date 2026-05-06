package it.asansonne.common.core.exception.custom;

import lombok.Getter;

/**
 * The type Inactive person exception.
 */
@Getter
public class UnauthorizedException extends RuntimeException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Not found exception.
   *
   * @param errorCode the errorCode
   * @param args      the args
   */
  public UnauthorizedException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}
