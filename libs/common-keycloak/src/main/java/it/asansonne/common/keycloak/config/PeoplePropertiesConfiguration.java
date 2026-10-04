package it.asansonne.common.keycloak.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(PeopleProperties.class)
@PropertySource("classpath:people-keycloak-defaults.properties")
public class PeoplePropertiesConfiguration {
}
