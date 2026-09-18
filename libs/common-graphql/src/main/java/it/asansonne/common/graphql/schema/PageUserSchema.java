package it.asansonne.common.graphql.schema;

import it.asansonne.common.graphql.dto.output.BaseOutput;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * The type Page User schema.
 * Don't use this class into the project
 * It is only a schema for Swagger documentation.
 */
@SuppressWarnings("unused")
public final class PageUserSchema extends PageImpl<BaseOutput> {

  /**
   * Instantiates a new Page user schema.
   *
   * @param content  the content
   * @param pageable the pageable
   * @param total    the total
   */
  public PageUserSchema(List<BaseOutput> content, Pageable pageable, long total) {
    super(content, pageable, total);
  }

  /**
   * Instantiates a new Page user schema.
   *
   * @param content the content
   */
  public PageUserSchema(List<BaseOutput> content) {
    super(content);
  }
}
