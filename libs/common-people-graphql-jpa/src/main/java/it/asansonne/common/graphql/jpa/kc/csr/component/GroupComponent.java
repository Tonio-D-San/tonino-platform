package it.asansonne.common.graphql.jpa.kc.csr.component;

import it.asansonne.common.graphql.component.CrudComponent;
import it.asansonne.common.graphql.jpa.kc.dto.input.CreateGroup;
import it.asansonne.common.graphql.jpa.kc.dto.input.FilterGroup;
import it.asansonne.common.graphql.jpa.kc.dto.input.UpdateGroup;
import it.asansonne.common.graphql.jpa.kc.dto.output.Group;

/**
 * The interface Business user component.
 */
public interface GroupComponent
    extends CrudComponent<Group, CreateGroup, FilterGroup, UpdateGroup> {
  Group findByRole(String name);
}
