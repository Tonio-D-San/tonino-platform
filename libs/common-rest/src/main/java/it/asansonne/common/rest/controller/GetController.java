package it.asansonne.common.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.rest.schema.PageUserSchema;
import java.security.Principal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

  @Operation(summary = "resource.find.by.uuid")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "resource.find.201.description"
      ), @ApiResponse(
      responseCode = "401",
      description = "resource.401.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "401 - UNAUTHORIZED",
                  value = """
                      {
                      "status": "UNAUTHORIZED",
                      "message": \
                      "Unauthorized message"
                       }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  ), @ApiResponse(
      responseCode = "403",
      description = "resource.403.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "403 - FORBIDDEN",
                  value = """
                      {
                      "status": "FORBIDDEN",
                      "message": \
                      "Forbidden message"
                      }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  ), @ApiResponse(
      responseCode = "404",
      description = "resource.404.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "404 - NOT FOUND",
                  value = """
                      {
                      "status": "NOT_FOUND",
                      "message": \
                      "Not found message"
                      , "validations": \
                      null }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  )
  })
  @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  S findByUuid(
      Principal principal,
      @Parameter(
          name = "uuid",
          description = "uuid",
          example = "081081d1-1777-4b59-8b4b-e9ab26186006"
      ) @PathVariable UUID uuid
  );

  @Operation(summary = "resource.find.all.by.param")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "resource.find.200.description"
      ), @ApiResponse(
      responseCode = "204",
      description = "resource.204.description",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
  ), @ApiResponse(
      responseCode = "401",
      description = "resource.401.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "401 - UNAUTHORIZED",
                  value = """
                      {
                      "status": "UNAUTHORIZED",
                      "message": \
                      "Unauthorized message"
                      , "validations": \
                      null }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  ), @ApiResponse(
      responseCode = "403",
      description = "resource.403.description",
      content = @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = {
              @ExampleObject(
                  name = "403 - FORBIDDEN",
                  value = """
                      {
                      "status": "FORBIDDEN",
                      "message": \
                      "Forbidden message"
                      , "validations": \
                      null }"""
              )
          },
          schema = @Schema(implementation = ExceptionMessage.class)
      )
  )
  })
  @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  Page<S> findByIsActive(
      Principal principal,
      @Parameter(name = "isActive", description = "isActive.description", example = "true") Boolean isActive,
      @Parameter(name = "page", description = "page.description", example = DEFAULT_PAGE)
      @RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
      @Parameter(name = "size", description = "size.description", example = DEFAULT_SIZE)
      @RequestParam(defaultValue = DEFAULT_SIZE) Integer size,
      @Parameter(name = "direction", description = "direction.description", example = DEFAULT_DIRECTION)
      @RequestParam(defaultValue = DEFAULT_DIRECTION) String direction
  );

  @Operation(summary = "resource.find.all")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "resource.find.200.description",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = PageUserSchema.class)
          )
      ),
      @ApiResponse(
          responseCode = "204",
          description = "resource.204.description",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ),
      @ApiResponse(
          responseCode = "401",
          description = "resource.401.description",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              examples = {
                  @ExampleObject(
                      name = "401 - UNAUTHORIZED",
                      value = """
                          {
                          "status": "UNAUTHORIZED",
                          "message": \
                          "Unauthorized message"
                          , "validations": \
                          null }"""
                  )
              },
              schema = @Schema(implementation = ExceptionMessage.class)
          )
      ),
      @ApiResponse(
          responseCode = "403",
          description = "resource.403.description",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              examples = {
                  @ExampleObject(
                      name = "403 - FORBIDDEN",
                      value = """
                          {
                          "status": "FORBIDDEN",
                          "message": \
                          "Forbidden message"
                          , "validations": \
                          null }"""
                  )
              },
              schema = @Schema(implementation = ExceptionMessage.class)
          )
      )
  })
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  Page<S> findAll(
      Principal principal,
      F filter,
      @Parameter(name = "page", description = "page.description", example = DEFAULT_PAGE)
      @RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
      @Parameter(name = "size", description = "size.description", example = DEFAULT_SIZE)
      @RequestParam(defaultValue = DEFAULT_SIZE) Integer size,
      @Parameter(name = "direction", description = "direction.description", example = DEFAULT_DIRECTION)
      @RequestParam(defaultValue = DEFAULT_DIRECTION) String direction
  );

}
