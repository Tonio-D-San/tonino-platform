package it.asansonne.peopleservice.ccsr.component;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.graphql.component.FindComponent;
import it.asansonne.peopleservice.dto.output.Group;

/**
 * The interface Business user component.
 */
public interface GroupComponent extends FindComponent<Group, Filter> {

}
