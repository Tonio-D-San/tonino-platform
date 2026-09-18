package it.asansonne.common.keycloak.utils;

import static it.asansonne.common.keycloak.enums.KcCredentialPayloadKey.TEMPORARY;
import static it.asansonne.common.keycloak.enums.KcCredentialPayloadKey.TYPE;
import static it.asansonne.common.keycloak.enums.KcCredentialPayloadKey.VALUE;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.CREDENTIALS;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.EMAIL;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.EMAIL_VERIFIED;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.ENABLED;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.FIRST_NAME;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.LAST_NAME;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.REQUIRED_ACTIONS;
import static it.asansonne.common.keycloak.enums.KcUserPayloadKey.USERNAME;

import it.asansonne.common.keycloak.dto.input.CreateKcUser;
import it.asansonne.common.keycloak.dto.input.UpdateKcUser;
import it.asansonne.common.keycloak.enums.RequiredAction;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RestCall {

  public static Map<String, Object> buildPayload(CreateKcUser request) {
    Map<String, Object> payload = new LinkedHashMap<>();

    EMAIL.put(payload, request.email());
    USERNAME.put(payload, request.email());
    FIRST_NAME.put(payload, request.name());
    LAST_NAME.put(payload, request.surname());
    ENABLED.put(payload, true);
    EMAIL_VERIFIED.put(payload, true);
    CREDENTIALS.put(payload, List.of(
        Map.of(
            TYPE.getKey(), "password",
            VALUE.getKey(), request.passwordTemp(),
            TEMPORARY.getKey(), true
        )
    ));
    if (request.requiredActions() != null && !request.requiredActions().isEmpty()) {
      REQUIRED_ACTIONS.put(
          payload,
          request.requiredActions()
              .stream()
              .map(RequiredAction::getValue)
              .toList()
      );
    }
    return payload;
  }

  public static Map<String, Object> buildPayload(UpdateKcUser request) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put(EMAIL.getKey(), request.email());
    return payload;
  }

  public static Map<String, Object> buildPayload(Boolean isEnabled) {
    Map<String, Object> payload = new LinkedHashMap<>();
    payload.put(ENABLED.getKey(), isEnabled);
    return payload;
  }
}
