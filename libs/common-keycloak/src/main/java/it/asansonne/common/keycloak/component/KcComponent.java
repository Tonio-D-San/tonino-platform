package it.asansonne.common.keycloak.component;

import it.asansonne.common.keycloak.dto.input.CreateKeycloakUser;
import it.asansonne.common.keycloak.dto.input.UpdateKeycloakUser;
import it.asansonne.common.keycloak.dto.output.KeycloakUser;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Business user component.
 */
public interface KcComponent {

  /**
   * Find by uuid keycloak user.
   *
   * @param uuid the uuid
   * @return the keycloak user
   */
  KeycloakUser findByUuid(UUID uuid);

  /**
   * Find by email keycloak user.
   *
   * @param email the email
   * @return the keycloak user
   */
  KeycloakUser findByEmail(String email);

  /**
   * Create business user create business user.
   *
   * @param user the business user
   * @return the creation of business user
   */
  KeycloakUser createKeycloakUser(CreateKeycloakUser user);

  /**
   * Update business user update business user.
   *
   * @param uuid         the uuid
   * @param user the business user
   * @return the update business user
   */
  KeycloakUser updateKeycloakUser(UUID uuid, UpdateKeycloakUser user);

  /**
   * Delete business user.
   *
   * @param uuid the uuid
   * @return the boolean
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

  Page<KeycloakUser> findAll(Pageable pageable);
}
