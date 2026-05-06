package it.asansonne.peopleservice.exception.custom;

import jakarta.persistence.PersistenceException;
import lombok.Getter;

/**
 * The type Didn't find exception.
 */
@Getter
public class LicenceException extends PersistenceException {

  private final String errorCode;
  private final transient Object[] args;

  /**
   * Instantiates a new Génova exception.
   *
   * @param errorCode the error message
   * @param args      the args
   */
  public LicenceException(String errorCode, Object... args) {
    super(errorCode);
    this.errorCode = errorCode;
    this.args = args == null ? new Object[0] : args;
  }
}