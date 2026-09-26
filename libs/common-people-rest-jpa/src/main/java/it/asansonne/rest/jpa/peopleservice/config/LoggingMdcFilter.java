package it.asansonne.rest.jpa.peopleservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The type Logging mdc filter.
 */
@Component
public class LoggingMdcFilter extends OncePerRequestFilter {

  private static final String CORRELATION_ID_HEADER = "x-correlation-id";
  @Value("${info.app.name}")
  private String serviceName;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    String correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID_HEADER))
        .filter(value -> !value.isBlank())
        .orElse(UUID.randomUUID().toString());

    try {
      MDC.put("serviceName", serviceName);
      MDC.put("correlationId", correlationId);
      MDC.put("requestPath", request.getRequestURI());

      response.setHeader(CORRELATION_ID_HEADER, correlationId);

      filterChain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
