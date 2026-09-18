package it.asansonne.common.graphql.dto.page;

import it.asansonne.common.core.dto.Dto;
import java.util.List;

@SuppressWarnings("unused")
public interface OutputPage<O extends Dto> extends Paginate {
  int page();
  int size();
  long totalElements();
  int totalPages();
  List<O> content();
}
