package it.asansonne.common.keycloak.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KcCredentialPayloadKey {

  TYPE("type"),
  VALUE("value"),
  TEMPORARY("temporary"),

  ID("id"),
  USER_LABEL("userLabel"),
  CREATED_DATE("createdDate"),
  CREDENTIAL_DATA("credentialData"),
  SECRET_DATA("secretData"),
  PRIORITY("priority");

  private final String key;
}