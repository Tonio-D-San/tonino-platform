package it.asansonne.common.rest.exception.handler;

import static it.asansonne.common.core.enums.ErrorMessage.CONFLICT_ERROR;
import static it.asansonne.common.core.enums.ErrorMessage.DATA_INTEGRITY;
import static it.asansonne.common.core.enums.ErrorMessage.UNCAUGHT_ERROR;

import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.core.exception.custom.ConflictException;
import it.asansonne.common.core.exception.custom.BadRequestException;
import it.asansonne.common.core.exception.custom.DataIntegrityException;
import it.asansonne.common.core.exception.custom.DuplicateFieldException;
import it.asansonne.common.core.exception.custom.ForbiddenException;
import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.core.exception.custom.NullStatusException;
import it.asansonne.common.core.exception.custom.OperationNotAllowedException;
import it.asansonne.common.core.exception.custom.UnauthorizedException;
import it.asansonne.common.core.exception.custom.UncaughtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ExceptionMessage> handleDataIntegrity(DataIntegrityViolationException ex) {
    log.warn("Database integrity constraint violated", ex);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ExceptionMessage(HttpStatus.CONFLICT, DATA_INTEGRITY.getCode()));
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<ExceptionMessage> handleOptimisticLock(OptimisticLockingFailureException ex) {
    log.warn("Concurrent database update conflict", ex);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ExceptionMessage(HttpStatus.CONFLICT, CONFLICT_ERROR.getCode()));
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ExceptionMessage> handleDataAccess(DataAccessException ex) {
    log.error("Database access failed", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, UNCAUGHT_ERROR.getCode()));
  }

  @ExceptionHandler(
      {ConflictException.class, DuplicateFieldException.class, DataIntegrityException.class}
  )
  public ResponseEntity<ExceptionMessage> handleConflict(RuntimeException ex) {
    return response(HttpStatus.CONFLICT, ex);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionMessage> handleBadRequest(BadRequestException ex) {
    return response(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ExceptionMessage> handleUnauthorized(UnauthorizedException ex) {
    return response(HttpStatus.UNAUTHORIZED, ex);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ExceptionMessage> handleForbidden(ForbiddenException ex) {
    return response(HttpStatus.FORBIDDEN, ex);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ExceptionMessage> handleNotFound(NotFoundException ex) {
    return response(HttpStatus.NOT_FOUND, ex);
  }

  @ExceptionHandler(OperationNotAllowedException.class)
  public ResponseEntity<ExceptionMessage> handleOperationNotAllowed(OperationNotAllowedException ex) {
    return response(HttpStatus.METHOD_NOT_ALLOWED, ex);
  }

  @ExceptionHandler({NullStatusException.class, UncaughtException.class})
  public ResponseEntity<ExceptionMessage> handleInternalError(RuntimeException ex) {
    return response(HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  private ResponseEntity<ExceptionMessage> response(HttpStatus status, RuntimeException ex) {
    return ResponseEntity.status(status).body(new ExceptionMessage(status, ex.getMessage()));
  }
}
