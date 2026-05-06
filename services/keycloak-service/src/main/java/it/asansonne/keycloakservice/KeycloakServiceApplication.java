package it.asansonne.keycloakservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "it.asansonne.keycloakservice",
    "it.asansonne.common"
})
public class KeycloakServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(KeycloakServiceApplication.class, args);
  }

}
