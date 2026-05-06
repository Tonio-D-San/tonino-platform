package it.asansonne.peopleservice.dto.output;

import it.asansonne.common.graphql.dto.output.Output;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Business user.
 */
@Builder
public record User(
    UUID uuid,
    Long createdAt,
    Long updatedAt,
    Boolean isActive,
    String name,
    String surname,
    String email,
    String phoneNumber,
    Group group
) implements Output {
}
