package it.asansonne.common.core.exception.custom;

import lombok.Getter;

/**
 * The type Didn't allow exception.
 */
@Getter
public class OperationNotAllowedException extends RuntimeException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Not implemented exception.
   *
   * @param errorCode the errorCode
   * @param args      the args
   */
  public OperationNotAllowedException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}
