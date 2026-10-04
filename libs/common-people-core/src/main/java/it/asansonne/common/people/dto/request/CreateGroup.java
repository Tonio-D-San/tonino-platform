package it.asansonne.common.people.dto.request;

import it.asansonne.common.core.dto.Create;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Create Group.
 */
@Builder
public record CreateGroup(

    @NotNull(message = "error.people.group.role.required")
    String role,

    UUID parentId,

    String description
) implements Create {
}
