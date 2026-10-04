package it.asansonne.rest.jpa.identityservice.csr.component;

import it.asansonne.common.identity.dto.request.CreateGroup;
import it.asansonne.common.identity.dto.request.FilterGroup;
import it.asansonne.common.identity.dto.request.UpdateGroup;
import it.asansonne.common.identity.dto.response.Group;
import it.asansonne.common.rest.component.CrudComponent;

/**
 * The interface Business user component.
 */
public interface GroupComponent
    extends CrudComponent<CreateGroup, UpdateGroup, FilterGroup, Group> {
}
