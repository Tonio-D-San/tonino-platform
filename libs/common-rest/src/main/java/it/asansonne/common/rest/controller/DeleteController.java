package it.asansonne.common.rest.controller;

import java.security.Principal;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
public interface DeleteController {
  /**
   * Delete topic by uuid.
   *
   * @param uuid the uuid
   */
  @DeleteMapping(value = "/{uuid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteByUuid(Principal principal, @PathVariable UUID uuid);
}
