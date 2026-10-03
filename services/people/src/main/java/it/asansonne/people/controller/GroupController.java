package it.asansonne.people.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.asansonne.common.core.exception.ExceptionMessage;
import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import it.asansonne.common.rest.controller.CrudController;
import java.security.Principal;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("isAuthenticated()")
@Tag(name = "Gruppi")
@ApiResponse(responseCode = "401", description = "Autenticazione richiesta", content = @Content)
@ApiResponse(responseCode = "403", description = "Accesso non consentito", content = @Content)
public interface GroupController extends CrudController<CreateGroup, UpdateGroup, FilterGroup, Group> {

  @Override
  @Operation(operationId = "groupFindByUuid", summary = "Cerca un gruppo tramite UUID (implementazione non completata)")
  @ApiResponse(responseCode = "200", description = "Gruppo trovato", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "404", description = "Gruppo non trovato",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  Group findByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "UUID del gruppo",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);

  @Override
  @Operation(operationId = "groupFindByIsActive", summary = "Elenca i gruppi per stato di attivazione (implementazione non completata)")
  @ApiResponse(responseCode = "200", description = "Pagina di gruppi", useReturnTypeSchema = true)
  Page<Group> findByIsActive(
      @Parameter(hidden = true) Principal principal,
      @Parameter(description = "Stato di attivazione da cercare", example = "true") Boolean isActive,
      @Parameter(description = "Indice della pagina, a partire da zero", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "Numero di elementi per pagina", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "Ordinamento per updatedAt: ASC o DESC", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "groupFindAll", summary = "Cerca i gruppi con filtri (implementazione non completata)")
  @ApiResponse(responseCode = "200", description = "Pagina di gruppi", useReturnTypeSchema = true)
  Page<Group> findAll(
      @Parameter(hidden = true) Principal principal, @ParameterObject FilterGroup filter,
      @Parameter(description = "Indice della pagina, a partire da zero", example = DEFAULT_PAGE) Integer page,
      @Parameter(description = "Numero di elementi per pagina", example = DEFAULT_SIZE) Integer size,
      @Parameter(description = "Ordinamento per updatedAt: ASC o DESC", example = DEFAULT_DIRECTION) String direction);

  @Override
  @Operation(operationId = "groupCreate", summary = "Crea un gruppo")
  @ApiResponse(responseCode = "201", description = "Gruppo creato", useReturnTypeSchema = true)
  @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  @ApiResponse(responseCode = "409", description = "Gruppo duplicato o conflitto con i vincoli dei dati",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = ExceptionMessage.class)))
  Group create(@Parameter(hidden = true) Principal principal, CreateGroup request);

  @Override
  @Operation(operationId = "groupUpdateByUuid", summary = "Aggiorna un gruppo (implementazione non completata)")
  @ApiResponse(responseCode = "200", description = "Operazione completata", content = @Content)
  void updateByUuid(@Parameter(description = "UUID del gruppo",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid, UpdateGroup request);

  @Override
  @Operation(operationId = "groupDeleteByUuid", summary = "Elimina un gruppo (implementazione non completata)")
  @ApiResponse(responseCode = "204", description = "Operazione completata senza contenuto", content = @Content)
  void deleteByUuid(@Parameter(hidden = true) Principal principal,
      @Parameter(description = "UUID del gruppo",
          example = "08fba211-60ca-45fc-b809-86bc2ad81dca") UUID uuid);
}
