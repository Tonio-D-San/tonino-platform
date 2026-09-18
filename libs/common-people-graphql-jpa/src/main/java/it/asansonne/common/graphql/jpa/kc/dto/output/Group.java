package it.asansonne.common.graphql.jpa.kc.dto.output;

import it.asansonne.common.graphql.dto.output.BaseOutput;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Group.
 */
@Builder
public record Group(
    UUID uuid,
    Long createdAt,
    Long updatedAt,
    Boolean isActive,
    String role,
    String path,
    String description,
    List<User> users
) implements BaseOutput {
}
