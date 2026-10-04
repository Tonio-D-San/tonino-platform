package it.asansonne.people.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Collections;
import it.asansonne.common.keycloak.config.PeopleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Open api configuration.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {
  private static final String SEC_SCHEME_OAUTH2 = "oauth2";
  @Value("${info.app.name}")
  private String appName;
  @Value("${application.host}")
  private String applicationHost;
  private final PeopleProperties properties;

  /**
   * Custom open api.
   *
   * @param appVersion     the app version
   * @return the open api
   */

  @Bean
  public OpenAPI customOpenApi(@Value("${info.app.version}") String appVersion) {
    var authUrl = getAuthUrl();
    return new OpenAPI()
        .servers(Collections.singletonList(new Server()
            .url(applicationHost)
            .description("people.api.server.description")))
        .info(new Info()
            .version(appVersion)
            .title(appName)
            .description("people.api.description")
            .version("v" + appVersion)
            .contact(new Contact()
                .name("Tonino platform Dev Team")
                .email("support@tonino.local")
                .url("https://github.com/asansonne/tonino-platform"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT"))
        ).components(new Components().addSecuritySchemes(
            SEC_SCHEME_OAUTH2,
            new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("common.security.oauth2.description")
                .flows(new OAuthFlows().authorizationCode(new OAuthFlow()
                    .authorizationUrl(authUrl + "/auth")
                    .tokenUrl(authUrl + "/token"))
                )
        )).security(Collections.singletonList(
            new SecurityRequirement().addList(SEC_SCHEME_OAUTH2)
        )).externalDocs(new ExternalDocumentation()
            .description("people.api.external.docs.description")
            .url("https://github.com/asansonne/tonino-platform/wiki")
        );
  }

  private String getAuthUrl() {
    return properties.keycloak().realmUrl() + "/protocol/openid-connect";
  }

}
