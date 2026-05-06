package it.asansonne.common.core.handler;


import it.asansonne.common.core.exception.ExceptionMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.ObjectMapper;

/**
 * The type Authorization and Authentication handler.
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthorizationAuthenticationHandler
    implements AccessDeniedHandler, AuthenticationEntryPoint {

  public static final String APPLICATION_JSON = "application/json";
  private final ObjectMapper objectMapper;

  @Override
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public void handle(HttpServletRequest request, @NonNull HttpServletResponse response,
                     @NonNull AccessDeniedException accessDeniedException)
      throws IOException {

    String acceptHeader = request.getHeader("Accept");

    if (acceptHeader != null && acceptHeader.contains(APPLICATION_JSON)) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType(APPLICATION_JSON);
      response.getWriter()
          .write(objectMapper
              .writeValueAsString(
                  new ExceptionMessage(HttpStatus.FORBIDDEN, accessDeniedException.getMessage())));
    } else {
      response.sendRedirect("/login");
    }
  }

  @Override
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(AuthenticationException.class)
  public void commence(HttpServletRequest request, @NonNull HttpServletResponse response,
                       @NonNull AuthenticationException authException) throws IOException {

    String acceptHeader = request.getHeader("Accept");

    if (acceptHeader != null && acceptHeader.contains(APPLICATION_JSON)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType(APPLICATION_JSON);
      response.getWriter()
          .write(objectMapper
              .writeValueAsString(
                  new ExceptionMessage(HttpStatus.UNAUTHORIZED, authException.getMessage())
              )
          );
    } else {
      response.sendRedirect("/login");
    }
  }

}
