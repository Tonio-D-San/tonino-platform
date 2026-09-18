package it.asansonne.rest.jpa.peopleservice.dto.request;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.rest.dto.Request;
import java.util.UUID;
import lombok.Builder;

/**
 * The type Organization filter input.
 */
@Builder
public record FilterGroup(
    UUID uuid,
    Boolean isActive,
    String role,
    String path,
    String description
) implements Filter, Request {
}
