package it.asansonne.common.keycloak.dto.output;

import it.asansonne.common.core.dto.Dto;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Keycloak user.
 */
@Builder
public record KeycloakUser(
    UUID id,
    String username,
    String firstName,
    String lastName,
    String email,
    Boolean emailVerified,
    Boolean enabled,
    Long createdTimestamp,
    List<String> requiredActions
) implements Dto {
}
