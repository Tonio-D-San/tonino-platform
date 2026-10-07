# Template e setup del realm Identity

Il file `identity-service.json` e' un template per il realm condiviso della piattaforma. Il Compose fornisce `APP_ID`, realm, ID dei quattro client, secret del client amministrativo, `APPLICATION_URL` e `FRONTEND_URL`. Audience e service account usano i client configurati esplicitamente. Il file viene montato con il nome `<realm>-realm.json`.

Per Identity Service usare `KEYCLOAK_REALM_NAME=tonino-platform`, `KEYCLOAK_CLIENT_ID=identity-api`, `KC_ADMIN_CLIENT_ID=identity-admin`, `KEYCLOAK_APP_CLIENT_ID=identity-swagger` e `KEYCLOAK_CONSOLE_CLIENT_ID=identity-console`. `APP_ID=identity-service` identifica il servizio, ma non genera realm o client.

Il template definisce tutti gli scope assegnati ai client, inclusi `basic` (claim `sub`), `profile`, `email`, `roles`, `web-origins` e `acr`, con i mapper standard di Keycloak 26.5. Durante l'import, una sezione `clientScopes` esplicita sostituisce la creazione degli scope standard: elencarli soltanto in `defaultClientScopes` non li crea. L'audience API e' uno scope di default; Swagger e console richiedono `openid profile email`.

Quando si aggiungono altri servizi alla piattaforma, riusare lo stesso realm e aggiungere client espliciti, per esempio `larp-api`, `larp-web`, `waystone-api` e `waystone-web`. Non derivare il realm da `APP_ID`.

## Realm nuovo

L'avvio Compose con `--import-realm` crea il realm e i client. Per un server Keycloak gia' in esecuzione, usare il comando seguente. Lo stesso comando aggiorna un realm gia' presente.

## Realm esistente o aggiornamento

Dalla radice del repository, visualizzare prima i nomi e l'operazione prevista, senza chiamare Keycloak:

```powershell
./services/keycloak/Initialize-IdentityRealm.ps1 -EnvFile services/identity-service/.env.dev -Profile dev -Plan
```

Applicare con un amministratore autorizzato a creare il realm e gestire i suoi client:

```powershell
./services/keycloak/Initialize-IdentityRealm.ps1 -EnvFile services/identity-service/.env.dev -Profile dev -AdminCredential (Get-Credential)
```

Per produzione selezionare `.env.prod` e `-Profile prod`. Il comando usa la stessa interpolazione Compose dell'avvio. `-AdminRealm` e' `master` per default; il login usa `admin-cli`. Le credenziali di provisioning non sono memorizzate nell'env e non vengono passate a Identity. L'URL pubblico di Keycloak deve essere raggiungibile dal computer che esegue il comando.

Il comando crea le risorse mancanti e aggiorna le impostazioni gestite dei quattro client, incluso `KC_ADMIN_CLIENT_SECRET`. Mantiene utenti, impostazioni del realm esistente, client estranei, attributi e mapper aggiuntivi. I redirect e le origini vengono aggiunti a quelli gia' presenti: rimuovere esplicitamente dalla console quelli obsoleti. Assicura il ruolo `manage-users` al service account amministrativo, senza duplicarlo. Il setup puo' essere rilanciato dopo un'interruzione; non e' una transazione unica. Per applicare correzioni agli scope su un ambiente esistente usare questo comando: il semplice riavvio con `--import-realm` salta il realm gia' presente e non lo aggiorna.

Se si cambiano gli ID, vengono creati nuovi client: i precedenti non vengono rinominati o rimossi automaticamente. `FRONTEND_URL` e `APPLICATION_URL` devono essere URL senza slash finale, query o fragment; per un frontend separato aggiornare anche CORS nell'applicazione.

Verifica offline del comando (API simulate):

```powershell
./services/keycloak/tests/Test-IdentityRealm.ps1
```

Riferimenti: [import e placeholder Keycloak](https://www.keycloak.org/server/importExport), [Admin REST API](https://www.keycloak.org/docs-api/latest/rest-api/index.html).
