package it.asansonne.common.keycloak.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class PeoplePropertiesTest {
  private final ApplicationContextRunner runner = new ApplicationContextRunner()
      .withUserConfiguration(PeoplePropertiesConfiguration.class)
      .withPropertyValues("people.keycloak.base-url=https://auth.example.com/auth/",
          "people.keycloak.admin-client-secret=test-secret");

  @Test
  void derivesNamesAndEndpointsWithoutThePeopleServiceProperties() {
    runner.withPropertyValues("people.app-id=gestionale").run(context -> {
      assertThat(context).hasNotFailed();
      var config = context.getBean(PeopleProperties.class).keycloak();
      assertThat(config.realm()).isEqualTo("gestionale");
      assertThat(config.apiClientId()).isEqualTo("gestionale-api");
      assertThat(config.adminClientId()).isEqualTo("gestionale-be");
      assertThat(config.frontendClientId()).isEqualTo("gestionale-fe");
      assertThat(config.realmUrl()).isEqualTo("https://auth.example.com/auth/realms/gestionale");
      assertThat(config.usersUrl()).isEqualTo("https://auth.example.com/auth/admin/realms/gestionale/users");
      assertThat(config.toString()).doesNotContain("test-secret");
    });
  }

  @Test
  void explicitNamesOverrideConventionsForAnExistingRealm() {
    runner.withPropertyValues("APP_ID=gestionale", "KEYCLOAK_REALM_NAME=existing",
        "KEYCLOAK_CLIENT_ID=existing-api", "KC_ADMIN_CLIENT_ID=existing-admin",
        "KEYCLOAK_APP_CLIENT_ID=existing-web").run(context -> {
          assertThat(context).hasNotFailed();
          var config = context.getBean(PeopleProperties.class);
          assertThat(config.appId()).isEqualTo("gestionale");
          assertThat(config.keycloak().realm()).isEqualTo("existing");
          assertThat(config.keycloak().apiClientId()).isEqualTo("existing-api");
          assertThat(config.keycloak().adminClientId()).isEqualTo("existing-admin");
          assertThat(config.keycloak().frontendClientId()).isEqualTo("existing-web");
        });
  }

  @Test
  void rejectsIncompleteOrAmbiguousConfiguration() {
    runner.withPropertyValues("people.keycloak.admin-client-secret=")
        .run(context -> assertThat(context).hasFailed());
    runner.withPropertyValues("people.keycloak.base-url=relative/path")
        .run(context -> assertThat(context).hasFailed());
    runner.withPropertyValues("people.keycloak.admin-client-id=people-api")
        .run(context -> assertThat(context).hasFailed());
  }
}
