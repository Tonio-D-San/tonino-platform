import type { AuthProviderProps } from "react-oidc-context";

const appOrigin =
  typeof window !== "undefined" ? window.location.origin : "http://localhost:3000";

export const oidcConfig: AuthProviderProps = {
  authority: `${process.env.NEXT_PUBLIC_KEYCLOAK_URL}/realms/${process.env.NEXT_PUBLIC_KEYCLOAK_REALM}`,
  client_id: process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID!,
  redirect_uri: appOrigin,
  post_logout_redirect_uri: appOrigin,
  response_type: "code",
  scope: "openid profile email identity-api-audience",
  automaticSilentRenew: true,
  onSigninCallback: () => {
    window.history.replaceState({}, document.title, window.location.pathname);
  },
};
