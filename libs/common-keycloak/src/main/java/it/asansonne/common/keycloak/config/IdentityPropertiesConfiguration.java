package it.asansonne.common.keycloak.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(IdentityProperties.class)
@PropertySource("classpath:identity-keycloak-defaults.properties")
public class IdentityPropertiesConfiguration {
}
