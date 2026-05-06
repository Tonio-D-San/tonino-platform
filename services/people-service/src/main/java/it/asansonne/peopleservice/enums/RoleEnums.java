package it.asansonne.peopleservice.enums;

/**
 * The interface Error enums.
 */
public interface RoleEnums {
  /**
   * Superuser role admin service role.
   */
  String SUPER_USER_ROLE = "hasRole('superadmin')";

  /**
   * The constant SUPER_USER_ADMIN_ROLE.
   */
  String SUPER_USER_ADMIN_ROLE = "hasRole('superadmin') or hasRole('admin')";
  /**
   * Admin role business user role.
   */
  String ADMIN_ROLE = "hasRole('admin')";

  /**
   * Operator role business user role.
   */
  String OPERATOR_ROLE = "hasRole('operator')";

  /**
   * Gets message.
   *
   * @return the message
   */
  String getRole();
}
