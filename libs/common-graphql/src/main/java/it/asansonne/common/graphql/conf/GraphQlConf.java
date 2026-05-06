package it.asansonne.common.graphql.conf;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * The type Graph ql conf.
 */
@Configuration
public class GraphQlConf {

  /**
   * Runtime wiring configurer runtime wiring configurer.
   *
   * @return the runtime wiring configurer
   */
  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.GraphQLLong);
  }
}
