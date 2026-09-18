package it.asansonne.common.rest.component;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.dto.Update;
import it.asansonne.common.rest.dto.Response;

@SuppressWarnings("unused")
public interface CrudComponent<C extends Create, U extends Update, F extends Filter, R extends Response>
    extends GetComponent<F, R>, PostComponent<C, R>, PatchComponent<U, R>, DeleteComponent {
}
