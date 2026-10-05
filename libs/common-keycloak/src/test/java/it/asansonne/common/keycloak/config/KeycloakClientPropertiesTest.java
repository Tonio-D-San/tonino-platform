package it.asansonne.common.keycloak.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class KeycloakClientPropertiesTest {
  private final ApplicationContextRunner runner = new ApplicationContextRunner()
      .withUserConfiguration(KeycloakClientPropertiesConfiguration.class)
      .withPropertyValues("keycloak.client.base-url=https://auth.example.com/auth/",
          "keycloak.client.realm=tonino-platform",
          "keycloak.client.api-client-id=gestionale-api",
          "keycloak.client.admin-client-id=gestionale-admin",
          "keycloak.client.frontend-client-id=gestionale-web",
          "keycloak.client.admin-client-secret=test-secret");

  @Test
  void requiresExplicitKeycloakConfiguration() {
    new ApplicationContextRunner()
        .withUserConfiguration(KeycloakClientPropertiesConfiguration.class)
        .run(context -> assertThat(context).hasFailed());
  }

  @Test
  void explicitNamesConfigureAReusableKeycloakClient() {
    runner.run(context -> {
      assertThat(context).hasNotFailed();
      var config = context.getBean(KeycloakClientProperties.class);
      assertThat(config.realm()).isEqualTo("tonino-platform");
      assertThat(config.apiClientId()).isEqualTo("gestionale-api");
      assertThat(config.adminClientId()).isEqualTo("gestionale-admin");
      assertThat(config.frontendClientId()).isEqualTo("gestionale-web");
      assertThat(config.realmUrl())
          .isEqualTo("https://auth.example.com/auth/realms/tonino-platform");
      assertThat(config.publicRealmUrl())
          .isEqualTo("https://auth.example.com/auth/realms/tonino-platform");
      assertThat(config.internalRealmUrl())
          .isEqualTo("https://auth.example.com/auth/realms/tonino-platform");
      assertThat(config.usersUrl())
          .isEqualTo("https://auth.example.com/auth/admin/realms/tonino-platform/users");
      assertThat(config.tokenUrl())
          .isEqualTo("https://auth.example.com/auth/realms/tonino-platform/protocol/openid-connect/token");
      assertThat(config.toString()).doesNotContain("test-secret");
    });
  }

  @Test
  void supportsSeparatePublicAndInternalUrls() {
    runner.withPropertyValues("keycloak.client.public-url=https://login.example.com",
        "keycloak.client.internal-url=http://keycloak:8080").run(context -> {
          var config = context.getBean(KeycloakClientProperties.class);
          assertThat(config.publicRealmUrl())
              .isEqualTo("https://login.example.com/realms/tonino-platform");
          assertThat(config.tokenUrl())
              .isEqualTo("http://keycloak:8080/realms/tonino-platform/protocol/openid-connect/token");
          assertThat(config.usersUrl())
              .isEqualTo("http://keycloak:8080/admin/realms/tonino-platform/users");
        });
  }

  @Test
  void rejectsIncompleteOrAmbiguousConfiguration() {
    runner.withPropertyValues("keycloak.client.admin-client-secret=")
        .run(context -> assertThat(context).hasFailed());
    runner.withPropertyValues("keycloak.client.base-url=relative/path")
        .run(context -> assertThat(context).hasFailed());
    runner.withPropertyValues("keycloak.client.admin-client-id=gestionale-api")
        .run(context -> assertThat(context).hasFailed());
  }
}
