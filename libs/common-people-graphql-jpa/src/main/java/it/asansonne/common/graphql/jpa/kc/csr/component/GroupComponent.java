package it.asansonne.common.graphql.jpa.kc.csr.component;

import it.asansonne.common.graphql.component.CrudComponent;
import it.asansonne.common.people.dto.request.CreateGroup;
import it.asansonne.common.people.dto.request.FilterGroup;
import it.asansonne.common.people.dto.request.UpdateGroup;
import it.asansonne.common.people.dto.response.Group;

/**
 * The interface Business user component.
 */
public interface GroupComponent
    extends CrudComponent<Group, CreateGroup, FilterGroup, UpdateGroup> {
  Group findByRole(String name);
}
