package it.asansonne.peopleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "it.asansonne.peopleservice",
    "it.asansonne.keycloakservice",
    "it.asansonne.common"
})
public class PeopleServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PeopleServiceApplication.class, args);
  }

}
