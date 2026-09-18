package it.asansonne.common.rest.mapper;

import it.asansonne.common.core.dto.Update;
import it.asansonne.common.core.model.Models;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * The interface Request mapper.
 *
 * @param <U> Dto parameter
 * @param <M> Model parameter
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface UpdateRequestMapper<U extends Update, M extends Models>  extends RequestMapper<M>{
  /**
   * To model m.
   *
   * @param request the request
   * @return the m
   */
  M updateToModel(U request);

  /**
   * To model a list.
   *
   * @param requests the requests
   * @return the list
   */
  default List<M> updateToModel(Collection<U> requests) {
    return requests == null ? Collections.emptyList() :
        requests.stream().map(this::updateToModel).toList();
  }

  /**
   * Convert a Dto with pageable into a model page.
   *
   * @param requests     the requests
   * @param pageable the pageable
   * @return the page
   */
  default Page<M> updateToModel(Page<U> requests, Pageable pageable) {
    if (requests == null) {
      return Page.empty();
    } else {
      List<M> modelList = requests.stream()
          .map(this::updateToModel)
          .toList();
      return new PageImpl<>(modelList, requests.getPageable(), requests.getTotalElements());
    }
  }

  default Page<M> updateToModel(Page<U> requests) {
    return requests == null
        ? Page.empty()
        : requests.map(this::updateToModel);
  }
}
