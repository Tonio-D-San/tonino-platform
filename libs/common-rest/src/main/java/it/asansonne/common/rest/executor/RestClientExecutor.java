package it.asansonne.common.rest.executor;

import it.asansonne.common.rest.exception.handler.RestErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * The type Rest client executor.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RestClientExecutor {

  private final RestTemplate restTemplate;

  /**
   * Exchange response entity.
   *
   * @param <T>          the type parameter
   * @param service      the service
   * @param url          the url
   * @param method       the method
   * @param entity       the entity
   * @param responseType the response type
   * @param errorHandler the error handler
   * @return the response entity
   */
  public <T> ResponseEntity<T> exchange(
      String service, String url, HttpMethod method,
      HttpEntity<?> entity, Class<T> responseType, RestErrorHandler errorHandler
  ) {
    try {
      return restTemplate.exchange(url, method, entity, responseType);
    } catch (HttpStatusCodeException ex) {
      log.error(
          "{} HTTP error: method={}, url={}, status={}, body={}",
          service, method, url, ex.getStatusCode(), ex.getResponseBodyAsString()
      );
      throw errorHandler.handle(url, ex);
    } catch (RestClientException ex) {
      log.error(
          "{} connection error: method={}, url={}, error={}", service, method, url, ex.getMessage()
      );
      throw errorHandler.handle(url, ex);
    }
  }
}
