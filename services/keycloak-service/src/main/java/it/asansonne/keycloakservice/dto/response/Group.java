package it.asansonne.keycloakservice.dto.response;

import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.keycloak.dto.input.UserRole;
import java.util.UUID;

public record Group(
    UUID uuid,
    String role,
    String path,
    String description)
    implements UserRole, Dto {
}
