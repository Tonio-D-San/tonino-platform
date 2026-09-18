package it.asansonne.common.graphql.jpa.kc.dto.page;

import it.asansonne.common.graphql.dto.page.OutputPage;
import it.asansonne.common.graphql.jpa.kc.dto.output.Group;
import java.util.List;
import lombok.Builder;

/**
 * The type Business user page.
 */
@Builder
public record GroupPage(
    List<Group> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) implements OutputPage<Group> {
}