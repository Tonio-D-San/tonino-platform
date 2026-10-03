package it.asansonne.common.keycloak.service;

import it.asansonne.common.keycloak.dto.input.CreateKcGroup;
import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcGroup;
import it.asansonne.common.keycloak.dto.output.KcUser;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface User service.
 */
public interface KcService {

  /**
   * Find by uuid keycloak user.
   *
   * @param uuid the uuid
   * @return the keycloak user
   */
  KcUser findUserByUuid(UUID uuid);

  /**
   * Find group by uuid kc group.
   *
   * @param uuid the uuid
   * @return the kc group
   */
  KcGroup findGroupByUuid(UUID uuid);


  /**
   * Read business user string.
   *
   * @param email the email
   * @return the string
   */
  KcUser findByEmail(String email);

  /**
   * Find all page.
   *
   * @param pageable the pageable
   * @return the page
   */
  Page<KcUser> findAll(Pageable pageable);

  /**
   * Find all groups list.
   *
   * @return the list
   */
  List<KcGroup> findAllGroups();

  /**
   * Create user create business user.
   *
   * @param request the request
   * @return the string
   */
  KcUser createKeycloakUser(CreateKcUser request);

  /**
   * Create kc group.
   *
   * @param request the request
   * @return the kc group
   */
  KcGroup createKcGroup(CreateKcGroup request);

  /**
   * Update user update business user.
   *
   * @param uuid    the uuid
   * @param request the request
   * @return the update business user
   */
  KcUser updateKeycloakUser(UUID uuid, UpdateKcUser request);

  /**
   * Delete business user.
   *
   * @param uuid the uuid
   */
  Boolean deleteKeycloakUser(UUID uuid);

  /**
   * Delete business user.
   *
   * @param uuid      the uuid
   * @param isEnabled the is enabled
   * @return the boolean
   */
  Boolean disableKeycloakUser(UUID uuid, Boolean isEnabled);

  /**
   * Add user to the group.
   *
   * @param userUuid  the user uuid
   * @param groupUuid the group uuid
   */
  void addUserToGroup(UUID userUuid, UUID groupUuid);
}
