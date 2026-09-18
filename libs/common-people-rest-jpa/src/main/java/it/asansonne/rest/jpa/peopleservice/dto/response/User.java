package it.asansonne.rest.jpa.peopleservice.dto.response;

import it.asansonne.common.rest.dto.Response;
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
) implements Response {
}
