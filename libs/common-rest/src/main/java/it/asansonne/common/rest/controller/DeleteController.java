package it.asansonne.common.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.asansonne.common.core.exception.ExceptionMessage;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
  @Operation(summary = "resource.delete.by.uuid")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "resource.204.description"
      ), @ApiResponse(
          responseCode = "401",
          description = "resource.401.description",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
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
              schema = @Schema(implementation = ExceptionMessage.class))),
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
  @DeleteMapping(value = "/{uuid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteByUuid(
      @Parameter(
          name = "uuid",
          description = "Topic uuid",
          example = "d8317c61-1ca9-4a3d-9501-ec70e74e50e6"
      ) @PathVariable UUID uuid
  );
}
