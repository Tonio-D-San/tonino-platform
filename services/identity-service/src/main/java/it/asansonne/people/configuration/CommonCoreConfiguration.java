package it.asansonne.people.configuration;

import it.asansonne.common.core.handler.AuthorizationAuthenticationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class CommonCoreConfiguration {

  @Bean
  public AuthorizationAuthenticationHandler authorizationAuthenticationHandler(
      ObjectMapper objectMapper
  ) {
    return new AuthorizationAuthenticationHandler(objectMapper);
  }
}
