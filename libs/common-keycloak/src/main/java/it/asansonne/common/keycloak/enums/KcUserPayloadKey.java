package it.asansonne.common.keycloak.enums;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KcUserPayloadKey {

  ID("id"),
  USERNAME("username"),
  FIRST_NAME("firstName"),
  LAST_NAME("lastName"),
  EMAIL("email"),
  EMAIL_VERIFIED("emailVerified"),
  ENABLED("enabled"),

  CREDENTIALS("credentials"),
  REQUIRED_ACTIONS("requiredActions"),

  ATTRIBUTES("attributes"),
  GROUPS("groups"),
  REALM_ROLES("realmRoles"),
  CLIENT_ROLES("clientRoles"),

  CREATED_TIMESTAMP("createdTimestamp"),
  FEDERATED_IDENTITIES("federatedIdentities"),
  SERVICE_ACCOUNT_CLIENT_ID("serviceAccountClientId"),
  DISABLEABLE_CREDENTIAL_TYPES("disableableCredentialTypes"),
  NOT_BEFORE("notBefore");

  private final String key;

  public void put(Map<String, Object> payload, Object value) {
    if (value != null) {
      payload.put(this.key, value);
    }
  }
}