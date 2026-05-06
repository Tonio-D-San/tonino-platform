package it.asansonne.common.keycloak.service;

import it.asansonne.common.keycloak.dto.input.CreateKeycloakUser;
import it.asansonne.common.keycloak.dto.input.UpdateKeycloakUser;
import it.asansonne.common.keycloak.dto.output.KeycloakUser;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface User service.
 */
public interface KeycloakService {

  /**
   * Find by uuid keycloak user.
   *
   * @param uuid the uuid
   * @return the keycloak user
   */
  KeycloakUser findByUuid(UUID uuid);

  /**
   * Read business user string.
   *
   * @param email the email
   * @return the string
   */
  KeycloakUser findByEmail(String email);

  /**
   * Find all page.
   *
   * @param pageable the pageable
   * @return the page
   */
  Page<KeycloakUser> findAll(Pageable pageable);

  /**
   * Create user create business user.
   *
   * @param request the request
   * @return the string
   */
  KeycloakUser createKeycloakUser(CreateKeycloakUser request);

  /**
   * Update user update business user.
   *
   * @param uuid    the uuid
   * @param request the request
   * @return the update business user
   */
  KeycloakUser updateKeycloakUser(UUID uuid, UpdateKeycloakUser request);

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
