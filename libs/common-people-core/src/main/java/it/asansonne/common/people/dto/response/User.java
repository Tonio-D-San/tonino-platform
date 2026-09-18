package it.asansonne.common.people.dto.response;

import it.asansonne.common.core.dto.Dto;
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
) implements Dto {
}
