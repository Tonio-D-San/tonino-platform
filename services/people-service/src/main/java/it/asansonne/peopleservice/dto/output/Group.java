package it.asansonne.peopleservice.dto.output;

import it.asansonne.common.graphql.dto.output.Output;
import it.asansonne.peopleservice.enums.UserRole;
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
    UserRole role,
    String path,
    String description,
    List<User> users
) implements Output {
}
