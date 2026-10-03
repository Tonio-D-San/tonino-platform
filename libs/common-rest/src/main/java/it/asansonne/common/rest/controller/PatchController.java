package it.asansonne.common.rest.controller;

import it.asansonne.common.core.dto.Update;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
public interface PatchController<U extends Update> {

  @PatchMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  void updateByUuid(
      @PathVariable UUID uuid, U request
  );
}
