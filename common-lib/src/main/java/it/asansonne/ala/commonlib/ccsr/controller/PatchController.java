package it.asansonne.ala.commonlib.ccsr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.asansonne.ala.commonlib.dto.Request;
import it.asansonne.authhub.exception.ExceptionMessage;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface PatchController<R extends Request> {

  @Operation(summary = "resource.update.summary")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "resource.update.200.description",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "resource.400.description",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              examples = {
                  @ExampleObject(
                      name = "400 - BAD REQUEST",
                      value = """
                          {
                          "status": "BAD REQUEST",
                          "message": \
                          "Bad Request message"
                          , "validations": \
                          {"field":"constraint violation message"}}"""
                  )
              },
              schema = @Schema(implementation = ExceptionMessage.class)
          )
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
                          }"""
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
                          }"""
                  )
              },
              schema = @Schema(implementation = ExceptionMessage.class))),
      @ApiResponse(responseCode = "404",
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
      ),
      @ApiResponse(
          responseCode = "405",
          description = "resource.405.description",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              examples = {
                  @ExampleObject(
                      name = "405 - METHOD NOT ALLOWED",
                      value = """
                          {
                          "status": "METHOD_NOT_ALLOWED",
                          "message": \
                          "Method not allowed message"
                          , "validations": \
                          null }"""
                  )
              },
              schema = @Schema(implementation = ExceptionMessage.class)
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "resource.409.description",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              examples = {
                  @ExampleObject(
                      name = "409 - CONFLICT",
                      value = """
                          {
                          "status": "CONFLICT",
                          "message": \
                          "Conflict message"
                          , "validations": \
                          null }"""
                  )
              },
              schema = @Schema(implementation = ExceptionMessage.class)))})
  @ResponseStatus(HttpStatus.OK)
  void updateByUuid(
      @PathVariable UUID uuid, R request
  );
}
