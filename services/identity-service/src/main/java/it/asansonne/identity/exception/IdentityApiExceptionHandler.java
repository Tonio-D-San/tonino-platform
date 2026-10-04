package it.asansonne.identity.exception;

import it.asansonne.common.core.enums.ErrorMessage;
import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class IdentityApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionMessage> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> validations = new LinkedHashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        validations.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.badRequest()
        .body(new ExceptionMessage(HttpStatus.BAD_REQUEST, ErrorMessage.BAD_REQUEST.getCode(), validations));
  }

  @ExceptionHandler(KeycloakCallException.class)
  public ResponseEntity<ExceptionMessage> handleKeycloakCall(KeycloakCallException ex) {
    log.warn("Keycloak operation failed: code={}, args={}", ex.getErrorCode(), ex.getArgs(), ex);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ExceptionMessage(HttpStatus.CONFLICT, ex.getErrorCode()));
  }
}
