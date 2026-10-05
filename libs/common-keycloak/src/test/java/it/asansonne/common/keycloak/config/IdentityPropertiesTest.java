package it.asansonne.common.keycloak.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class IdentityPropertiesTest {
  private final ApplicationContextRunner runner = new ApplicationContextRunner()
      .withUserConfiguration(IdentityPropertiesConfiguration.class)
      .withPropertyValues("identity.keycloak.base-url=https://auth.example.com/auth/",
          "identity.keycloak.admin-client-secret=test-secret");

  @Test
  void requiresExplicitRealmAndClientNames() {
    runner.withPropertyValues("identity.app-id=gestionale").run(context ->
        assertThat(context).hasFailed());
  }

  @Test
  void explicitNamesConfigureThePlatformRealm() {
    runner.withPropertyValues("APP_ID=identity-service", "KEYCLOAK_REALM_NAME=tonino-platform",
        "KEYCLOAK_CLIENT_ID=identity-api", "KC_ADMIN_CLIENT_ID=identity-admin",
        "KEYCLOAK_APP_CLIENT_ID=identity-swagger").run(context -> {
          assertThat(context).hasNotFailed();
          var config = context.getBean(IdentityProperties.class);
          assertThat(config.appId()).isEqualTo("identity-service");
          assertThat(config.keycloak().realm()).isEqualTo("tonino-platform");
          assertThat(config.keycloak().apiClientId()).isEqualTo("identity-api");
          assertThat(config.keycloak().adminClientId()).isEqualTo("identity-admin");
          assertThat(config.keycloak().frontendClientId()).isEqualTo("identity-swagger");
          assertThat(config.keycloak().realmUrl())
              .isEqualTo("https://auth.example.com/auth/realms/tonino-platform");
          assertThat(config.keycloak().usersUrl())
              .isEqualTo("https://auth.example.com/auth/admin/realms/tonino-platform/users");
          assertThat(config.keycloak().toString()).doesNotContain("test-secret");
        });
  }

  @Test
  void rejectsIncompleteOrAmbiguousConfiguration() {
    runner.withPropertyValues("identity.keycloak.admin-client-secret=")
        .run(context -> assertThat(context).hasFailed());
    runner.withPropertyValues("identity.keycloak.base-url=relative/path")
        .run(context -> assertThat(context).hasFailed());
    runner.withPropertyValues("identity.keycloak.admin-client-id=identity-api")
        .run(context -> assertThat(context).hasFailed());
  }
}
