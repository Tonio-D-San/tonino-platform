package it.asansonne.peopleservice.exception.handler;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import it.asansonne.common.core.exception.custom.BadRequestException;
import it.asansonne.common.core.exception.custom.ForbiddenException;
import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.common.core.exception.custom.NullStatusException;
import it.asansonne.common.core.exception.custom.OperationNotAllowedException;
import it.asansonne.common.core.exception.custom.UnauthorizedException;
import it.asansonne.common.keycloak.exception.KeycloakCallException;
import it.asansonne.peopleservice.exception.custom.ConflictException;
import it.asansonne.peopleservice.exception.custom.DataIntegrityException;
import it.asansonne.peopleservice.exception.custom.DuplicateFieldException;
import it.asansonne.peopleservice.exception.custom.LicenceException;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

/**
 * The type Graph ql exception resolver.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GraphQlExceptionResolver extends DataFetcherExceptionResolverAdapter {

  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, @NonNull DataFetchingEnvironment env) {
    switch (ex) {
      case BadRequestException e -> {
        log.warn("GraphQL bad request: code={}, args={}", e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.BAD_REQUEST)
            .build();
      }
      case NotFoundException e -> {
        log.warn("GraphQL not found: code={}, args={}", e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.NOT_FOUND)
            .build();
      }
      case ConflictException e -> {
        log.warn("GraphQL conflict: code={}, args={}", e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.BAD_REQUEST)
            .build();
      }
      case DuplicateFieldException e -> {
        log.warn("GraphQL duplicate field: code={}, args={}", e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.BAD_REQUEST)
            .build();
      }
      case ConstraintViolationException e -> {
        log.warn("Constraint violation in GraphQL resolver", e);
        return GraphqlErrorBuilder.newError(env)
            .message(buildConstraintViolationMessage(e))
            .errorType(ErrorType.BAD_REQUEST)
            .build();
      }
      case KeycloakCallException e -> {
        log.warn("Keycloak call error in GraphQL resolver: code={}, args={}",
            e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.INTERNAL_ERROR)
            .build();
      }
      case IllegalArgumentException e -> {
        log.warn("Illegal argument in GraphQL resolver", e);
        return GraphqlErrorBuilder.newError(env)
            .message(e.getMessage())
            .errorType(ErrorType.BAD_REQUEST)
            .build();
      }
      case DataIntegrityException e -> {
        log.warn("Data integrity violation", e);
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.BAD_REQUEST)
            .build();
      }
      case AccessDeniedException e -> {
        log.warn("Access denied in GraphQL resolver", e);
        return GraphqlErrorBuilder.newError(env)
            .message("Access denied")
            .errorType(ErrorType.FORBIDDEN)
            .build();
      }
      case ForbiddenException e -> {
        log.warn("GraphQL forbidden: code={}, args={}", e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.BAD_REQUEST)
            .build();
      }
      case UnauthorizedException e -> {
        log.warn("Unauthorized access in GraphQL resolver: code={}, args={}",
            e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.UNAUTHORIZED)
            .build();
      }
      case NullStatusException e -> {
        log.warn("Null HTTP status code in GraphQL resolver: code={}, args={}",
            e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.INTERNAL_ERROR)
            .build();
      }
      case OperationNotAllowedException e -> {
        log.warn("Method not implemented: code={}, args={}",
            e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.INTERNAL_ERROR)
            .build();
      }
      case LicenceException e -> {
        log.warn("Too much users in organization: code={}, args={}",
            e.getErrorCode(), e.getArgs());
        return GraphqlErrorBuilder.newError(env)
            .extensions(Map.of(
                "path", e.getErrorCode(),
                "code", e.getArgs(),
                "type", e.getClass().getSimpleName()
            ))
            .errorType(ErrorType.INTERNAL_ERROR)
            .build();
      }
      default -> {
        log.error("GraphQL internal error", ex);
        return GraphqlErrorBuilder.newError(env)
            .message("Uncaught exception: " + ex.getMessage() + "\n" + ex.getClass().getName())
            .errorType(ErrorType.INTERNAL_ERROR)
            .build();
      }
    }
  }

  private String buildConstraintViolationMessage(ConstraintViolationException e) {
    return e.getConstraintViolations().stream()
        .map(v -> {
          String path = v.getPropertyPath() == null ? "" : v.getPropertyPath().toString();
          return path.isBlank() ? v.getMessage() : path + ": " + v.getMessage();
        })
        .distinct()
        .reduce((a, b) -> a + "; " + b)
        .orElse("Validation error");
  }
}