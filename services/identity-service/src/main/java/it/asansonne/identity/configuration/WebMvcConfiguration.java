package it.asansonne.identity.configuration;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Web mvc configuration.
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
  @Value("${api.base-path}")
  private String apiBasePath;
  @Value("${server.cors.allowed.methods:GET,POST,PATCH,DELETE}")
  private String allowedMethods;
  @Value("${server.cors.allowed.origins:*}")
  private String allowedOrigins;
  @Value("${server.cors.allowed.headers:*}")
  private String allowedHeaders;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping(apiBasePath + "/**")
        .allowedOrigins(split(allowedOrigins))
        .allowedMethods(split(allowedMethods))
        .allowedHeaders(split(allowedHeaders));
  }

  private static String[] split(String value) {
    return Arrays.stream(value.split(","))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .toArray(String[]::new);
  }
}
