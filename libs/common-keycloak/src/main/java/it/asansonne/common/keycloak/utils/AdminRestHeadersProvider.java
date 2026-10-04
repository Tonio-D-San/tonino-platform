package it.asansonne.common.keycloak.utils;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import it.asansonne.common.keycloak.config.IdentityProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AdminRestHeadersProvider implements RestHeadersProvider {

  private final IdentityProperties properties;

  private final RestTemplate restTemplate;

  @Override
  public HttpHeaders build() {
    HttpHeaders formHeaders = new HttpHeaders();
    formHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("grant_type", "client_credentials");
    form.add("client_id", properties.keycloak().adminClientId());
    form.add("client_secret", properties.keycloak().adminClientSecret());

    ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
        properties.keycloak().realmUrl() + "/protocol/openid-connect/token",
        HttpMethod.POST,
        new HttpEntity<>(form, formHeaders),
        new ParameterizedTypeReference<>() {
        }
    );

    Map<String, Object> body = response.getBody();
    Object accessToken = body == null ? null : body.get("access_token");
    if (accessToken == null) {
      throw new IllegalStateException("Missing Keycloak admin access token");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken.toString());
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
