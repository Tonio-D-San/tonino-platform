package it.asansonne.common.graphql.mapper;

import it.asansonne.common.graphql.dto.input.Create;
import it.asansonne.common.graphql.dto.input.Update;
import it.asansonne.common.jpa.model.BaseModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The interface Input mappers.
 *
 * @param <C> the type parameter
 * @param <U> the type parameter
 * @param <M> the type parameter
 */
@SuppressWarnings("unused")
public interface InputMapper<C extends Create, U extends Update, M extends BaseModel> {
  /**
   * To model m.
   *
   * @param input the input
   * @return the m
   */
  M toModel(C input);

  /**
   * To model m.
   *
   * @param input the input
   * @return the m
   */
  M toModel(U input);

  /**
   * To model list.
   *
   * @param inputs the inputs
   * @return the list
   */
  default List<M> toModel(Collection<C> inputs) {
    return inputs == null ? Collections.emptyList() :
        inputs.stream().map(this::toModel).toList();
  }
}
