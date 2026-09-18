package it.asansonne.common.keycloak.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequiredAction {

  VERIFY_EMAIL("VERIFY_EMAIL"),
  UPDATE_PROFILE("UPDATE_PROFILE"),
  UPDATE_PASSWORD("UPDATE_PASSWORD"),
  CONFIGURE_TOTP("CONFIGURE_TOTP"),
  TERMS_AND_CONDITIONS("TERMS_AND_CONDITIONS"),

  // Keycloak/User Profile
  VERIFY_PROFILE("VERIFY_PROFILE"),
  UPDATE_EMAIL("UPDATE_EMAIL"),

  // Recovery codes / WebAuthn
  CONFIGURE_RECOVERY_AUTHN_CODES("CONFIGURE_RECOVERY_AUTHN_CODES"),
  WEBAUTHN_REGISTER("webauthn-register"),
  WEBAUTHN_REGISTER_PASSWORDLESS("webauthn-register-passwordless"),

  // Azioni più specifiche / AIA
  UPDATE_USER_LOCALE("update_user_locale"),
  DELETE_CREDENTIAL("delete_credential"),
  IDP_LINK("idp_link"),
  VERIFIABLE_CREDENTIAL_OFFER("verifiable_credential_offer");

  private final String value;
}