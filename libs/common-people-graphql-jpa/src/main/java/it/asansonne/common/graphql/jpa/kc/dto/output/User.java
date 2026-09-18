package it.asansonne.common.graphql.jpa.kc.dto.output;

import it.asansonne.common.graphql.dto.output.BaseOutput;
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
) implements BaseOutput {
}
