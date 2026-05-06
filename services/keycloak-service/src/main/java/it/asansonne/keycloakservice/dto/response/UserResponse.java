package it.asansonne.keycloakservice.dto.response;

import it.asansonne.common.rest.dto.Response;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(
    UUID uuid,
    String username,
    String firstName,
    String lastName,
    String email,
    Boolean emailVerified,
    Boolean enabled,
    Long createdTimestamp,
    List<String> requiredActions
) implements Response {
}
