package it.asansonne.rest.jpa.peopleservice.dto.request;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.rest.dto.Request;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record CreateGroup(

    @NotNull
    String role,

    @NotNull
    String path,

    String description
) implements Create, Request {
}
