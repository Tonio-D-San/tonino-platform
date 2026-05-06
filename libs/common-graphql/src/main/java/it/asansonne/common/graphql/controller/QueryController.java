package it.asansonne.common.graphql.controller;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.graphql.dto.output.Output;
import it.asansonne.common.graphql.dto.page.OutputPage;
import java.security.Principal;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;

@SuppressWarnings("unused")
public interface QueryController<O extends Output, F extends Filter> {
  /**
   * The constant UPDATED_AT.
   */
  String UPDATED_AT = "updatedAt";

  O findByUuid(Principal principal, @Argument UUID uuid);

  OutputPage<O> findByIsActive(Principal principal, @Argument Boolean isActive);

  OutputPage<O> findAll(
      Principal principal, @Argument F filter,
      @Argument Integer page, @Argument Integer size, @Argument String direction
  );

}
