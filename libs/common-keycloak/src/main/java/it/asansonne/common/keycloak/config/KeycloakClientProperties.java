package it.asansonne.common.keycloak.config;

import java.net.URI;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

/** Configuration for a reusable Keycloak client integration. */
@ConfigurationProperties("keycloak.client")
public record KeycloakClientProperties(String baseUrl, String publicUrl, String internalUrl,
                                       String realm, String apiClientId,
                                       String adminClientId, String frontendClientId,
                                       String adminClientSecret) {
  public KeycloakClientProperties {
    baseUrl = normalizeRequiredUrl(baseUrl, "keycloak.client.base-url");
    publicUrl = normalizeOptionalUrl(publicUrl, "keycloak.client.public-url", baseUrl);
    internalUrl = normalizeOptionalUrl(internalUrl, "keycloak.client.internal-url", baseUrl);
    Assert.hasText(realm, "keycloak.client.realm is required");
    Assert.hasText(apiClientId, "keycloak.client.api-client-id is required");
    Assert.hasText(adminClientId, "keycloak.client.admin-client-id is required");
    Assert.hasText(frontendClientId, "keycloak.client.frontend-client-id is required");
    Assert.isTrue(!apiClientId.equals(adminClientId) && !apiClientId.equals(frontendClientId)
        && !adminClientId.equals(frontendClientId), "Keycloak client IDs must be distinct");
    Assert.hasText(adminClientSecret, "keycloak.client.admin-client-secret is required");
  }

  public String realmUrl() {
    return url(baseUrl, "realms", realm);
  }

  public String publicRealmUrl() {
    return url(publicUrl, "realms", realm);
  }

  public String internalRealmUrl() {
    return url(internalUrl, "realms", realm);
  }

  public String adminUrl() {
    return url(internalUrl, "admin", "realms", realm);
  }

  public String usersUrl() {
    return adminUrl() + "/users";
  }

  public String groupsUrl() {
    return adminUrl() + "/groups";
  }

  public String tokenUrl() {
    return internalRealmUrl() + "/protocol/openid-connect/token";
  }

  private static String normalizeRequiredUrl(String value, String property) {
    Assert.hasText(value, property + " is required");
    return normalizeUrl(value, property);
  }

  private static String normalizeOptionalUrl(String value, String property, String fallback) {
    if (value == null || value.isBlank()) {
      return fallback;
    }
    return normalizeUrl(value, property);
  }

  private static String normalizeUrl(String value, String property) {
    URI uri = URI.create(value);
    Assert.isTrue(("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))
            && uri.getHost() != null && uri.getQuery() == null && uri.getFragment() == null
            && uri.getUserInfo() == null,
        property + " must be an absolute HTTP(S) URL without credentials, query or fragment");
    while (value.endsWith("/")) {
      value = value.substring(0, value.length() - 1);
    }
    return value;
  }

  private static String url(String baseUrl, String... segments) {
    return UriComponentsBuilder.fromUriString(baseUrl).pathSegment(segments)
        .build().encode().toUriString();
  }

  @Override
  @NonNull
  public String toString() {
    return "KeycloakClientProperties[baseUrl=" + baseUrl + ", publicUrl=" + publicUrl
        + ", internalUrl=" + internalUrl + ", realm=" + realm
        + ", apiClientId=" + apiClientId + ", adminClientId=" + adminClientId
        + ", frontendClientId=" + frontendClientId + ", adminClientSecret=<redacted>]";
  }
}
