# Template e setup del realm People

Il file `people.json` e' un template condiviso. Il Compose fornisce APP_ID, realm, ID dei tre client, secret backend, APPLICATION_URL e FRONTEND_URL. Audience e service account usano gli stessi valori dell'applicazione. Il file viene montato con il nome `<realm>-realm.json`.

Per una nuova app copiare `services/people/.env.template`, impostare `APP_ID=gestionale` e compilare URL/credenziali. Il realm sara' `gestionale`; i client saranno `gestionale-api`, `gestionale-be` e `gestionale-fe`. Non e' necessario creare o modificare un JSON per ogni applicazione.

Gli env locali precedenti hanno override espliciti per mantenere `people-realm` e `people-client-*`. Per adottare la convenzione su un nuovo realm rimuovere KEYCLOAK_REALM_NAME, KEYCLOAK_CLIENT_ID, KC_ADMIN_CLIENT_ID e KEYCLOAK_APP_CLIENT_ID, poi cambiare APP_ID. Questo crea nuove identita': non migra gli utenti del vecchio realm.

## Realm nuovo

L'avvio Compose con `--import-realm` crea il realm e i client. Per un server Keycloak gia' in esecuzione, usare il comando seguente. Lo stesso comando aggiorna un realm gia' presente.

## Realm esistente o aggiornamento

Dalla radice del repository, visualizzare prima i nomi e l'operazione prevista, senza chiamare Keycloak:

```powershell
./services/keycloak/Initialize-PeopleRealm.ps1 -EnvFile services/people/.env.dev -Profile dev -Plan
```

Applicare con un amministratore autorizzato a creare il realm e gestire i suoi client:

```powershell
./services/keycloak/Initialize-PeopleRealm.ps1 -EnvFile services/people/.env.dev -Profile dev -AdminCredential (Get-Credential)
```

Per produzione selezionare `.env.prod` e `-Profile prod`. Il comando usa la stessa interpolazione Compose dell'avvio. `-AdminRealm` e' `master` per default; il login usa `admin-cli`. Le credenziali di provisioning non sono memorizzate nell'env e non vengono passate a People. L'URL pubblico di Keycloak deve essere raggiungibile dal computer che esegue il comando.

Il comando crea le risorse mancanti e aggiorna le impostazioni gestite dei tre client, incluso il secret backend configurato. Mantiene utenti, impostazioni del realm esistente, client estranei, attributi e mapper aggiuntivi. I redirect e le origini vengono aggiunti a quelli gia' presenti: rimuovere esplicitamente dalla console quelli obsoleti. Assicura il ruolo `manage-users` al service account backend, senza duplicarlo. Il setup puo' essere rilanciato dopo un'interruzione; non e' una transazione unica.

Se si cambiano gli ID, vengono creati nuovi client: i precedenti non vengono rinominati o rimossi automaticamente. `FRONTEND_URL` e `APPLICATION_URL` devono essere URL senza slash finale, query o fragment; per un frontend separato aggiornare anche CORS nell'applicazione.

Verifica offline del comando (API simulate):

```powershell
./services/keycloak/tests/Test-PeopleRealm.ps1
```

Riferimenti: [import e placeholder Keycloak](https://www.keycloak.org/server/importExport), [Admin REST API](https://www.keycloak.org/docs-api/latest/rest-api/index.html).
