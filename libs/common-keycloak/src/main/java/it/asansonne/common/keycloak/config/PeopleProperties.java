package it.asansonne.common.keycloak.config;

import java.net.URI;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

/** Configuration of one application's People integration (one realm per instance). */
@ConfigurationProperties("people")
public record PeopleProperties(String appId, Keycloak keycloak) {
  public PeopleProperties {
    Assert.hasText(appId, "people.app-id is required");
    Assert.isTrue(appId.matches("[a-z][a-z0-9-]*"),
        "people.app-id must contain lowercase letters, digits or hyphens and start with a letter");
    Assert.notNull(keycloak, "people.keycloak configuration is required");
  }

  public record Keycloak(String baseUrl, String realm, String apiClientId,
                         String adminClientId, String frontendClientId,
                         String adminClientSecret) {
    public Keycloak {
      Assert.hasText(baseUrl, "people.keycloak.base-url is required");
      URI uri = URI.create(baseUrl);
      Assert.isTrue(("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))
              && uri.getHost() != null && uri.getQuery() == null && uri.getFragment() == null
              && uri.getUserInfo() == null,
          "people.keycloak.base-url must be an absolute HTTP(S) URL without credentials, query or fragment");
      while (baseUrl.endsWith("/")) {
        baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
      }
      Assert.hasText(realm, "people.keycloak.realm is required");
      Assert.hasText(apiClientId, "people.keycloak.api-client-id is required");
      Assert.hasText(adminClientId, "people.keycloak.admin-client-id is required");
      Assert.hasText(frontendClientId, "people.keycloak.frontend-client-id is required");
      Assert.isTrue(!apiClientId.equals(adminClientId) && !apiClientId.equals(frontendClientId)
          && !adminClientId.equals(frontendClientId), "People client IDs must be distinct");
      Assert.hasText(adminClientSecret, "people.keycloak.admin-client-secret is required");
    }

    public String realmUrl() {
      return url("realms", realm);
    }

    public String adminUrl() {
      return url("admin", "realms", realm);
    }

    public String usersUrl() {
      return adminUrl() + "/users";
    }

    public String groupsUrl() {
      return adminUrl() + "/groups";
    }

    private String url(String... segments) {
      return UriComponentsBuilder.fromUriString(baseUrl).pathSegment(segments)
          .build().encode().toUriString();
    }

    @Override
    @NonNull
    public String toString() {
      return "Keycloak[baseUrl=" + baseUrl + ", realm=" + realm
          + ", apiClientId=" + apiClientId + ", adminClientId=" + adminClientId
          + ", frontendClientId=" + frontendClientId + ", adminClientSecret=<redacted>]";
    }
  }
}
