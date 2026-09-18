package it.asansonne.rest.jpa.peopleservice.dto.request;

import it.asansonne.common.core.dto.Update;
import it.asansonne.common.rest.dto.Request;
import lombok.Builder;

/**
 * The type Create Business User.
 */
@Builder
public record UpdateGroup(
    String description
) implements Update, Request {
}
