package it.asansonne.peopleservice.exception.custom;

import lombok.Getter;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * The type Duplicate field exception.
 */
@Getter
public class DataIntegrityException extends DataIntegrityViolationException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Duplicate field exception.
   *
   * @param errorCode the errorCode
   * @param args    the args
   */
  public DataIntegrityException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}
