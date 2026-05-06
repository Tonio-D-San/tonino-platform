package it.asansonne.peopleservice.ccsr.service;

import it.asansonne.common.core.dto.Filter;
import it.asansonne.common.jpa.service.FindService;
import it.asansonne.peopleservice.enums.UserRole;
import it.asansonne.peopleservice.model.GroupModel;
import java.util.List;

/**
 * The interface Business user component.
 */
public interface GroupService extends FindService<GroupModel, Filter> {

  /**
   * Group by name group.
   *
   * @param name the name
   * @return the group
   */
  GroupModel findByRole(UserRole name);

  /**
   * Sync groups list.
   *
   * @param groups the groups
   * @return the list
   */
  List<GroupModel> syncGroups(List<GroupModel> groups);
}
