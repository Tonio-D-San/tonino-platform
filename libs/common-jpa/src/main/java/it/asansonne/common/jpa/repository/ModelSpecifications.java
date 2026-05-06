package it.asansonne.common.jpa.repository;

import it.asansonne.common.jpa.util.SpecificationUtils;
import it.asansonne.common.core.model.Models;
import it.asansonne.common.core.dto.Filter;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("unused")
public interface ModelSpecifications<M extends Models, F extends Filter> {

  Specification<M> withFilter(F filter);

  default Specification<M> likeIfValid(String attributeName, String searchValue) {
    return SpecificationUtils.likeIgnoringShortValue(attributeName, searchValue);
  }

}