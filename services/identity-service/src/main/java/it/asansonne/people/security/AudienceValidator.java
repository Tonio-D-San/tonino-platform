package it.asansonne.people.security;

import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

@RequiredArgsConstructor
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
  private final String requiredAudience;

  @Override
  @NonNull
  public OAuth2TokenValidatorResult validate(Jwt jwt) {
    if (Objects.requireNonNull(jwt.getAudience()).contains(requiredAudience)) {
      return OAuth2TokenValidatorResult.success();
    }
    return OAuth2TokenValidatorResult.failure(
        new OAuth2Error(
            "invalid_token",
            "Required audience '" + requiredAudience + "' is missing",
            null
        )
    );
  }
}
