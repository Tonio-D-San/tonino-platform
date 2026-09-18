package it.asansonne.common.graphql.jpa.kc.dto.input;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.graphql.dto.input.Input;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record CreateGroup(

    @NotNull
    @Size(max = 255, message = "name troppo lungo")
    String role,

    @NotNull
    @Size(max = 255, message = "path troppo lungo")
    String path,

    @Size(max = 255, message = "description troppo lunga")
    String description
) implements Create, Input {
}
