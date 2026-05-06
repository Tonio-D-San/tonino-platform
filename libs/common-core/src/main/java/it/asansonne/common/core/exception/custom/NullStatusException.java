package it.asansonne.common.core.exception.custom;

import lombok.Getter;

/**
 * The type Didn't find exception.
 */
@Getter
public class NullStatusException extends RuntimeException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Not found exception.
   *
   * @param errorCode the message
   * @param args      the args
   */
  public NullStatusException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}