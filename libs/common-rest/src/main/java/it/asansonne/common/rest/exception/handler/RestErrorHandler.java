package it.asansonne.common.rest.exception.handler;

import org.springframework.web.client.RestClientException;

/**
 * The interface Rest error handler.
 */
public interface RestErrorHandler {

  /**
   * Handle runtime exception.
   *
   * @param url       the url
   * @param exception the exception
   * @return the runtime exception
   */
  RuntimeException handle(String url, RestClientException exception);
}
