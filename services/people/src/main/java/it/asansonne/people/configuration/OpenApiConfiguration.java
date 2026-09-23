package it.asansonne.people.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
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
import java.util.Collections;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * The type Open api configuration.
 */
@Configuration
@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8082", description = "AuthHub API")})
public class OpenApiConfiguration {
  private static final String SEC_SCHEME_OAUTH2 = "oauth2";
  @Value("${info.app.name}")
  private String appName;
  @Value("${keycloak.host}")
  private String authServer;
  @Value("${keycloak.realm.name}")
  private String realm;

  /**
   * Custom open api.
   *
   * @param appDescription the app description
   * @param appVersion     the app version
   * @return the open api
   */

  @Bean
  public OpenAPI customOpenApi(@Value("${info.app.description}") String appDescription,
                               @Value("${info.app.version}") String appVersion) {
    var authUrl = getAuthUrl();
    return new OpenAPI()
        .info(new Info()
            .version(appVersion)
            .title("Welcome in " + appName)
            .description(appDescription +
                """
                  <div style="font-size: 15px; line-height: 1.5;">
                    <b>Tonino-platform</b> è il servizio centralizzato per gestire autenticazioni via Google e altri provider social.<br>
                    <ul>
                      <li>Login semplificato (OAuth2 Social)</li>
                      <li>Gestione utenti interna</li>
                      <li>API documentate e pronte all’integrazione</li>
                    </ul>
                  </div>
                """
            )
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
                    .description("Oauth2 flow")
                    .flows(new OAuthFlows().authorizationCode(new OAuthFlow()
                        .authorizationUrl(authUrl + "/auth")
                        .tokenUrl(authUrl + "/token"))
                    )
        )).security(Collections.singletonList(
            new SecurityRequirement().addList(SEC_SCHEME_OAUTH2))
        ).externalDocs(new ExternalDocumentation()
            .description("Documentazione estesa e guide di integrazione")
            .url("https://github.com/asansonne/tonino-platform/wiki")
        );
  }

  @Bean
  public OpenApiCustomizer i18nOpenApiCustomizer(MessageSource messageSource) {
    return openApi -> openApi.getPaths().forEach((_, item) ->
        item.readOperations().forEach(operation -> {
          if (operation.getDescription() != null) {
            operation.setDescription(
                messageSource.getMessage(
                    operation.getDescription(),
                    null,
                    LocaleContextHolder.getLocale()
                )
            );
          }
        })
    );
  }

  private String getAuthUrl() {
    return String.format("%s/realms/%s/protocol/openid-connect",
        this.authServer, this.realm);
  }

}
