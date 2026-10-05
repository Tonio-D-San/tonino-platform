package it.asansonne.common.keycloak.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KeycloakClientProperties.class)
public class KeycloakClientPropertiesConfiguration {
}
