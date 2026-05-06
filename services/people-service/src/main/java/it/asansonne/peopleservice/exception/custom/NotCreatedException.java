package it.asansonne.peopleservice.exception.custom;

import lombok.Getter;

/**
 * The type Argument exception.
 */
@Getter
public class NotCreatedException extends RuntimeException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Not found exception.
   *
   * @param errorCode the message
   * @param args      the args
   */
  public NotCreatedException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}