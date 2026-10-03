package it.asansonne.common.rest.controller;

import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Filter;
import java.security.Principal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
public interface GetController<F extends Filter, S extends Dto> {
  /**
   * The constant UPDATED_AT.
   */
  String UPDATED_AT = "updatedAt";
  String DEFAULT_PAGE = "0";
  String DEFAULT_SIZE = "20";
  String DEFAULT_DIRECTION = "ASC";

  @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  S findByUuid(
      Principal principal,
      @PathVariable UUID uuid
  );

  @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  Page<S> findByIsActive(
      Principal principal,
      Boolean isActive,
      @RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(defaultValue = DEFAULT_SIZE) Integer size,
      @RequestParam(defaultValue = DEFAULT_DIRECTION) String direction
  );

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  Page<S> findAll(
      Principal principal,
      @ModelAttribute F filter,
      @RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(defaultValue = DEFAULT_SIZE) Integer size,
      @RequestParam(defaultValue = DEFAULT_DIRECTION) String direction
  );

}
