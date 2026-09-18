package it.asansonne.common.keycloak.utils;

import org.springframework.http.HttpHeaders;

/**
 * The interface Rest headers provider.
 */
public interface RestHeadersProvider {
  /**
   * Build http headers.
   *
   * @return the http headers
   */
  HttpHeaders build();
}
