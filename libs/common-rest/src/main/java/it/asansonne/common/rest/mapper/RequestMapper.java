package it.asansonne.common.rest.mapper;

import it.asansonne.common.core.model.Models;
import it.asansonne.common.rest.dto.Request;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * The interface Request mapper.
 *
 * @param <D> Dto parameter
 * @param <M> Model parameter
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface RequestMapper<D extends Request, M extends Models> {

  /**
   * To model m.
   *
   * @param request the request
   * @return the m
   */
  M toModel(D request);

  /**
   * To model a list.
   *
   * @param requests the requests
   * @return the list
   */
  default List<M> toModel(Collection<D> requests) {
    return requests == null ? Collections.emptyList() :
        requests.stream().map(this::toModel).toList();
  }

  /**
   * Convert a Dto with pageable into a model page.
   *
   * @param requests     the requests
   * @param pageable the pageable
   * @return the page
   */
  default Page<M> toModel(Page<D> requests, Pageable pageable) {
    if (requests == null) {
      return Page.empty();
    } else {
      List<M> modelList = requests.stream()
          .map(this::toModel)
          .toList();
      return new PageImpl<>(modelList, requests.getPageable(), requests.getTotalElements());
    }
  }

  default Page<M> toModel(Page<D> requests) {
    return requests == null
        ? Page.empty()
        : requests.map(this::toModel);
  }

}
