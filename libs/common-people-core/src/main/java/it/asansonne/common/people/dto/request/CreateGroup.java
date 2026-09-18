package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Create;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * The type Create Group.
 */
@Builder
public record CreateGroup(

    @NotNull
    String role,

    @NotNull
    String path,

    String description
) implements Create {
}
