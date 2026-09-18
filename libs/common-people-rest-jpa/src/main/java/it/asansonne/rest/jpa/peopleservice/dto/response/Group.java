package it.asansonne.rest.jpa.peopleservice.dto.response;

import it.asansonne.common.rest.dto.Response;
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
    String role,
    String path,
    String description,
    List<User> users
) implements Response {
}
