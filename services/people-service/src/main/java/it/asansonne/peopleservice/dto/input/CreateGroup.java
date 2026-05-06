package it.asansonne.peopleservice.dto.input;

import it.asansonne.common.graphql.dto.input.Create;
import it.asansonne.peopleservice.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record CreateGroup(

    @NotNull
    UserRole role,

    @NotNull
    String path,

    String description
) implements Create {
}
