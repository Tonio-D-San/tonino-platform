package it.asansonne.common.graphql.component;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.dto.Update;

@SuppressWarnings("unused")
public interface CrudComponent<O extends Dto, I extends Create, F extends Filter, U extends Update>
    extends
    CreateComponent<I, O>, FindComponent<O, F>, UpdateComponent<O, U>, DeleteComponent {
}
