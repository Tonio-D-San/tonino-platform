package it.asansonne.common.keycloak.component;

import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.dto.output.KcUser;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Business user component.
 */
@SuppressWarnings("unused")
public interface KcComponent {

  /**
   * Find by uuid keycloak user.
   *
   * @param uuid the uuid
   * @return the keycloak user
   */
  KcUser findByUuid(UUID uuid);

  /**
   * Find by email keycloak user.
   *
   * @param email the email
   * @return the keycloak user
   */
  KcUser findByEmail(String email);

  /**
   * Create business user create business user.
   *
   * @param user the business user
   * @return the creation of business user
   */
  KcUser createKcUser(CreateKcUser user);

  /**
   * Update business user update business user.
   *
   * @param uuid         the uuid
   * @param user the business user
   * @return the update business user
   */
  KcUser updateKcUser(UUID uuid, UpdateKcUser user);

  /**
   * Delete business user.
   *
   * @param uuid the uuid
   * @return the boolean
   */
  Boolean deleteKcUser(UUID uuid);

  /**
   * Delete business user.
   *
   * @param uuid      the uuid
   * @param isEnabled the is enabled
   * @return the boolean
   */
  Boolean disableKcUser(UUID uuid, Boolean isEnabled);

  Page<KcUser> findAll(Pageable pageable);
}
