package it.asansonne.rest.jpa.identityservice.autoconfigure;

import it.asansonne.rest.jpa.identityservice.IdentityRestJpaModule;
import it.asansonne.rest.jpa.identityservice.csr.repository.UserRepository;
import it.asansonne.rest.jpa.identityservice.model.UserModel;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@ConditionalOnClass(UserRepository.class)
@ComponentScan(basePackageClasses = IdentityRestJpaModule.class)
@EntityScan(basePackageClasses = UserModel.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableScheduling
public class CommonIdentityRestJpaAutoConfiguration {
}
