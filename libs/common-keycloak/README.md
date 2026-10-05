# Keycloak client riutilizzabile

Un'applicazione puo' aggiungere `it.asansonne:common-keycloak:1.1.0` alle dipendenze Maven. Il modulo registra componenti e servizi Keycloak tramite autoconfigurazione, ma non carica default applicativi e non assume nomi di servizi, realm o variabili d'ambiente.

Configurazione minima per l'integrazione Keycloak:

```properties
keycloak.client.base-url=https://auth.example.com
keycloak.client.realm=tonino-platform
keycloak.client.api-client-id=gestionale-api
keycloak.client.admin-client-id=gestionale-admin
keycloak.client.frontend-client-id=gestionale-web
keycloak.client.admin-client-secret=${GESTIONALE_KEYCLOAK_ADMIN_SECRET}
```

`base-url`, realm, client API, client amministrativo, client frontend e secret sono obbligatori. La configurazione viene verificata all'avvio. Se l'issuer pubblico e l'endpoint interno sono diversi, configurarli esplicitamente:

```properties
keycloak.client.public-url=https://login.example.com
keycloak.client.internal-url=http://keycloak:8080
```

Se `public-url` o `internal-url` non sono valorizzati, la libreria usa `base-url`.

Esempio di mapping nel servizio ospitante:

```properties
keycloak.client.base-url=${KEYCLOAK_URL}
keycloak.client.public-url=${KEYCLOAK_PUBLIC_URL:${KEYCLOAK_URL}}
keycloak.client.internal-url=${KEYCLOAK_INTERNAL_URL:${KEYCLOAK_URL}}
keycloak.client.realm=${KEYCLOAK_REALM_NAME}
keycloak.client.api-client-id=${KEYCLOAK_CLIENT_ID}
keycloak.client.admin-client-id=${KC_ADMIN_CLIENT_ID}
keycloak.client.frontend-client-id=${KEYCLOAK_APP_CLIENT_ID}
keycloak.client.admin-client-secret=${KC_ADMIN_CLIENT_SECRET}
```

La libreria espone `KeycloakClientProperties`; per l'autenticazione JWT usare `publicRealmUrl()` come issuer, per le chiamate server-to-server usare `tokenUrl()` o gli URL admin generati dalla stessa classe. I ruoli API restano sotto `resource_access[apiClientId]`.

L'app ospitante deve comunque configurare datasource, schema, endpoint REST, SecurityFilterChain, Swagger e regole di autorizzazione. Il setup Keycloak e' separato dal runtime: seguire [la guida di provisioning](../../services/keycloak/README.md). Il backend riceve `realm-management/manage-users` nel realm dell'app; non riceve credenziali di amministrazione del server.

Ogni contesto applicativo usa un solo realm. Per piu' applicazioni indipendenti configurare istanze/datasource separate.
