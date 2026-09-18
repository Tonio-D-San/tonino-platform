package it.asansonne.common.rest.exception.handler.impl;

import static it.asansonne.common.core.enums.ErrorMessage.BAD_REQUEST;
import static it.asansonne.common.core.enums.ErrorMessage.CONFLICT_ERROR;
import static it.asansonne.common.core.enums.ErrorMessage.FORBIDDEN;
import static it.asansonne.common.core.enums.ErrorMessage.NULL_HTTP_STATUS_CODE;
import static it.asansonne.common.core.enums.ErrorMessage.UNAUTHORIZED_ACCESS;
import static it.asansonne.common.core.enums.ErrorMessage.UNCAUGHT_ERROR;
import static it.asansonne.common.core.enums.ErrorMessage.URL_NOT_FOUND;

import it.asansonne.common.core.exception.custom.BadRequestException;
import it.asansonne.common.core.exception.custom.ConflictException;
import it.asansonne.common.core.exception.custom.ForbiddenException;
import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.core.exception.custom.NullStatusException;
import it.asansonne.common.core.exception.custom.UnauthorizedException;
import it.asansonne.common.core.exception.custom.UncaughtException;
import it.asansonne.common.rest.exception.handler.RestErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

/**
 * The type Keycloak rest error handler.
 */
@Component
public class RestErrorHandlerImpl implements RestErrorHandler {
  @Override
  public RuntimeException handle(String url, RestClientException exception) {
    if (exception instanceof HttpStatusCodeException ex) {
      HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
      return switch (status) {
        case null -> new NullStatusException(NULL_HTTP_STATUS_CODE.getCode());
        case UNAUTHORIZED -> new UnauthorizedException(UNAUTHORIZED_ACCESS.getCode());
        case FORBIDDEN -> new ForbiddenException(FORBIDDEN.getCode());
        case NOT_FOUND -> new NotFoundException(URL_NOT_FOUND.getCode(), url);
        case BAD_REQUEST -> new BadRequestException(BAD_REQUEST.getCode(), ex.getResponseBodyAsString());
        case CONFLICT -> new ConflictException(CONFLICT_ERROR.getCode());
        default -> new UncaughtException(UNCAUGHT_ERROR.getCode(), status);
      };
    }
    return new UncaughtException(UNCAUGHT_ERROR.getCode(), url, exception.getMessage());
  }
}
