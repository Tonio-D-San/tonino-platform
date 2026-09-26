package it.asansonne.common.keycloak.autoconfigure;

import it.asansonne.common.keycloak.component.KcComponent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnClass(KcComponent.class)
@ComponentScan(basePackages = "it.asansonne.common.keycloak")
public class CommonKeycloakAutoConfiguration {
}
