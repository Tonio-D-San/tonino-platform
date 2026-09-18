package it.asansonne.common.graphql.component;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.dto.Update;
import it.asansonne.common.graphql.dto.output.Output;

@SuppressWarnings("unused")
public interface CrudComponent<O extends Output, I extends Create, F extends Filter, U extends Update>
    extends
    CreateComponent<I, O>, FindComponent<O, F>, UpdateComponent<O, U>, DeleteComponent {
}
