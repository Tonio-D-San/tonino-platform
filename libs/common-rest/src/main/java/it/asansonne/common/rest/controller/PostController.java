package it.asansonne.common.rest.controller;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Dto;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
public interface PostController<C extends Create, S extends Dto> {

  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  S create(Principal principal, @RequestBody C request);
}
