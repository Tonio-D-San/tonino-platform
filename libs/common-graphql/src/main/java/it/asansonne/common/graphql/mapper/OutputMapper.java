package it.asansonne.common.graphql.mapper;

import it.asansonne.common.graphql.dto.output.Output;
import it.asansonne.common.graphql.dto.page.Paginate;
import it.asansonne.common.jpa.model.BaseModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * The interface Output mappers.
 *
 * @param <M> the type parameter
 * @param <O> the type parameter
 * @param <P> the type parameter
 */
@SuppressWarnings("unused")
public interface OutputMapper<M extends BaseModel, O extends Output, P extends Paginate> {
  /**
   * To dto o.
   *
   * @param model the model
   * @return the o
   */
  O toDto(M model);

  /**
   * To dto list.
   *
   * @param models the models
   * @return the list
   */
  default List<O> toDto(Collection<M> models) {
    return models == null ? Collections.emptyList() :
        models.stream().map(this::toDto).toList();
  }

  /**
   * To dto page.
   *
   * @param models the models
   * @return the page
   */
  default Page<O> toDto(Page<M> models) {
    if (models == null) {
      return Page.empty();
    } else {
      List<O> dtoList = models.stream()
          .map(this::toDto)
          .toList();
      return new PageImpl<>(dtoList, models.getPageable(), models.getTotalElements());
    }
  }

  /**
   * To little dto o.
   *
   * @param model the model
   * @return the o
   */
  O toLittleDto(M model);

  /**
   * To little DTO list.
   *
   * @param models the models
   * @return the list
   */
  default List<O> toLittleDto(Collection<M> models) {
    return models == null ? Collections.emptyList() :
        models.stream().map(this::toLittleDto).toList();
  }

  /**
   * To page p.
   *
   * @param model the model
   * @return the p
   */
  P toPage(Page<M> model);

  /**
   * To a little page.
   *
   * @param models the models
   * @return the page
   */
  default Page<O> toLittlePage(Page<M> models) {
    if (models == null) {
      return Page.empty();
    } else {
      List<O> dtoList = models.stream()
          .map(this::toLittleDto)
          .toList();
      return new PageImpl<>(dtoList, models.getPageable(), models.getTotalElements());
    }
  }
}
