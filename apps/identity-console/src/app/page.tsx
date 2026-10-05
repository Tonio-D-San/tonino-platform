"use client";

import { useAuth } from "react-oidc-context";

export default function Home() {
  const auth = useAuth();

  if (auth.isLoading) {
    return <main className="p-8">Caricamento...</main>;
  }

  if (auth.error) {
    return <main className="p-8">Errore: {auth.error.message}</main>;
  }

  if (!auth.isAuthenticated) {
    return (
      <main className="flex min-h-screen items-center justify-center bg-zinc-50 p-8">
        <section className="w-full max-w-md rounded-2xl border border-zinc-200 bg-white p-8 shadow-sm">
          <p className="text-sm font-medium uppercase tracking-wide text-zinc-500">
            Tonino Platform
          </p>
          <h1 className="mt-3 text-3xl font-bold text-zinc-950">
            Identity Console
          </h1>
          <p className="mt-3 text-zinc-600">
            Accedi con Keycloak per iniziare a gestire utenti e gruppi.
          </p>
          <button
            className="mt-8 rounded bg-black px-4 py-2 font-medium text-white transition hover:bg-zinc-800"
            onClick={() => void auth.signinRedirect()}
          >
            Login
          </button>
        </section>
      </main>
    );
  }

  return (
    <main className="space-y-4 p-8">
      <p className="text-sm font-medium uppercase tracking-wide text-zinc-500">
        Tonino Platform
      </p>
      <h1 className="text-2xl font-bold">Identity Console</h1>
      <div>Utente: {auth.user?.profile.email ?? auth.user?.profile.preferred_username}</div>
      <button
        className="rounded border border-zinc-300 px-4 py-2 transition hover:bg-zinc-50"
        onClick={() => void auth.signoutRedirect()}
      >
        Logout
      </button>
    </main>
  );
}
