package it.asansonne.common.rest.autoconfigure;

import it.asansonne.common.rest.exception.handler.RestErrorHandler;
import it.asansonne.common.rest.exception.handler.impl.RestErrorHandlerImpl;
import it.asansonne.common.rest.executor.RestClientExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@ConditionalOnClass(RestClientExecutor.class)
public class CommonRestAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  @ConditionalOnMissingBean
  public RestErrorHandler restErrorHandler() {
    return new RestErrorHandlerImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public RestClientExecutor restClientExecutor(RestTemplate restTemplate) {
    return new RestClientExecutor(restTemplate);
  }
}
