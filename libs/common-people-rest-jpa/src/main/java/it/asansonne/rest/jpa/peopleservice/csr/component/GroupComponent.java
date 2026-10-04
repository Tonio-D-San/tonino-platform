package it.asansonne.rest.jpa.peopleservice.csr.component;

import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;
import it.asansonne.common.rest.component.CrudComponent;

/**
 * The interface Business user component.
 */
public interface GroupComponent
    extends CrudComponent<CreateGroup, UpdateGroup, FilterGroup, Group> {
}
