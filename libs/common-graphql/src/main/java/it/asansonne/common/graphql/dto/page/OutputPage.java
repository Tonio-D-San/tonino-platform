package it.asansonne.common.graphql.dto.page;

import it.asansonne.common.graphql.dto.output.Output;
import java.util.List;
import lombok.Builder;

/**
 * The type Business user page.
 */
@Builder
public record OutputPage<O extends Output>(
    List<O> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) implements Paginate {
}