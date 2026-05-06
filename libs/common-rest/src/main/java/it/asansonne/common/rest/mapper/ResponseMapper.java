package it.asansonne.common.rest.mapper;

import it.asansonne.common.core.model.Models;
import it.asansonne.common.rest.dto.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * The interface Response mapper.
 *
 * @param <M> Model parameter
 * @param <D> Dto parameter
 */
@SuppressWarnings("unused")
public interface ResponseMapper<M extends Models, D extends Response> {

  /**
   * To a dto list.
   *
   * @param models the models
   * @return the list
   */
  default List<D> toDto(Collection<M> models) {
    return models == null ? Collections.emptyList() :
        models.stream().map(this::toDto).toList();
  }

  /**
   * To a dto page.
   *
   * @param models   the models
   * @param pageable the pageable
   * @return the page
   */
  default Page<D> toDto(Page<M> models, Pageable pageable) {
    if (models == null) {
      return Page.empty();
    } else {
      List<D> dtoList = models.stream()
          .map(this::toDto)
          .toList();
      return new PageImpl<>(dtoList, pageable, models.getTotalElements());
    }
  }

  default Page<D> toDto(Page<M> requests) {
    return requests == null
        ? Page.empty()
        : requests.map(this::toDto);
  }

  /**
   * To dto d.
   *
   * @param model the model
   * @return the d
   */
  D toDto(M model);

}
