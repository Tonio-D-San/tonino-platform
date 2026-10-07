"use client";

import { AuthProvider as OidcProvider } from "react-oidc-context";
import { oidcConfig } from "@/lib/auth/oidc";

export function AuthProvider({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return <OidcProvider {...oidcConfig}>{children}</OidcProvider>;
}
