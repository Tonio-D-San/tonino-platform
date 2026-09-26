package it.asansonne.common.core.autoconfigure;

import it.asansonne.common.core.handler.AuthorizationAuthenticationHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
@ConditionalOnClass(AuthorizationAuthenticationHandler.class)
public class CommonCoreAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public AuthorizationAuthenticationHandler authorizationAuthenticationHandler(
      ObjectMapper objectMapper
  ) {
    return new AuthorizationAuthenticationHandler(objectMapper);
  }
}
