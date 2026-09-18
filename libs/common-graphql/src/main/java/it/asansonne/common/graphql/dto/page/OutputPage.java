package it.asansonne.common.graphql.dto.page;

import it.asansonne.common.graphql.dto.output.Output;
import java.util.List;

@SuppressWarnings("unused")
public interface OutputPage<O extends Output> extends Paginate {
  int page();
  int size();
  long totalElements();
  int totalPages();
  List<O> content();
}