package it.asansonne.common.rest.component;

import it.asansonne.common.core.dto.Create;
import it.asansonne.common.core.dto.Dto;
import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.core.dto.Update;

@SuppressWarnings("unused")
public interface CrudComponent<C extends Create, U extends Update, F extends Filter, R extends Dto>
    extends GetComponent<F, R>, PostComponent<C, R>, PatchComponent<U, R>, DeleteComponent {
}
