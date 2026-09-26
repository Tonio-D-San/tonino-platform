package it.asansonne.rest.jpa.peopleservice.autoconfigure;

import it.asansonne.rest.jpa.peopleservice.PeopleRestJpaModule;
import it.asansonne.rest.jpa.peopleservice.csr.repository.UserRepository;
import it.asansonne.rest.jpa.peopleservice.model.UserModel;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@ConditionalOnClass(UserRepository.class)
@ComponentScan(basePackageClasses = PeopleRestJpaModule.class)
@EntityScan(basePackageClasses = UserModel.class)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableScheduling
public class CommonPeopleRestJpaAutoConfiguration {
}
