# Avvio di Identity Service

I file Compose sono una base comune con due override. Eseguire dalla radice del repository:

```powershell
docker compose --env-file services/identity-service/.env.dev -f services/identity-service/compose.yml -f services/identity-service/compose.dev.yml up --build -d
```

Per produzione sostituire entrambe le occorrenze di `dev` con `prod`.
`--env-file` serve a Compose per interpolare le variabili; il container riceve quelle dichiarate in `environment`.
Gli env locali sono ignorati da Git. `.env.template` documenta le variabili e va compilato con le credenziali.
`MAVEN_SETTINGS_FILE` deve indicare un settings.xml esistente con accesso al repository Maven GitHub Packages.
La build Docker usa soltanto Identity Service come contesto. Maven scarica le librerie pubblicate su GitHub Packages usando il repository dichiarato nel POM e le credenziali del secret `maven_settings`. Prima di costruire il servizio con le nuove proprieta' `identity.*`, pubblicare le librerie aggiornate e allineare le versioni delle dipendenze nel POM: le modifiche ai sorgenti locali delle librerie non vengono incluse nell'immagine.

## Sviluppo

- Profilo Spring `dev`, aggiornamento automatico dello schema e Swagger abilitati.
- Identity Service: http://localhost:8082/swagger-ui/index.html.
- Keycloak: http://keycloak.localhost:5443. Il nome deve risolvere a 127.0.0.1 sul computer; se necessario aggiungere `127.0.0.1 keycloak.localhost` al file hosts. Nel container e' un alias di rete di Keycloak, sulla stessa porta 5443. Usare questo URL anche per ottenere i token.
- PostgreSQL: localhost:5433; Mailpit: http://localhost:8025 (non configura automaticamente SMTP nel realm).
- SonarQube e' opzionale: aggiungere `--profile quality` prima di `up`.

Per eseguire Identity Service dall'IDE avviare soltanto `postgres keycloak mailpit` con lo stesso comando Compose e importare `.env.dev` nella configurazione di esecuzione. Spring non carica automaticamente i file `.env`; senza profilo esplicito usa `dev`.

## Produzione

Compilare `.env.prod` con domini HTTPS reali al posto di example.com e credenziali adeguate. L'override presume un reverse proxy sull'host: Identity Service e Keycloak pubblicano porte soltanto su loopback; PostgreSQL non pubblica porte. Il dominio pubblico di Keycloak deve essere raggiungibile anche dal container Identity Service, per discovery, validazione JWT e chiamate amministrative.

Il profilo `prod` valida lo schema senza modificarlo: predisporre le tabelle con il processo di migrazione prima di avviare Identity su un database nuovo. In questo repository non e' ancora configurato un migratore automatico. Swagger e dettagli health sono disabilitati; Keycloak usa `start`, con TLS terminato dal proxy e header X-Forwarded impostati dal proxy.

## Dati e autenticazione

I progetti Compose `tonino-platform-dev` e `tonino-platform-prod` hanno volumi separati. Eventuali volumi del precedente progetto `tonino-platform` non vengono riutilizzati automaticamente: trasferire i dati se necessario.

`services/db/init-db.sh` crea soltanto i database applicativo e Keycloak usando le variabili dell'ambiente. Viene eseguito solo su un volume PostgreSQL vuoto. Il precedente `init-db.sql` resta disponibile ma non viene piu' montato. Cambiare un env non aggiorna password o ruoli in database gia' inizializzati.

Il realm Keycloak e i client sono espliciti. Per la piattaforma usare `KEYCLOAK_REALM_NAME=tonino-platform`, `KEYCLOAK_CLIENT_ID=identity-api`, `KEYCLOAK_APP_CLIENT_ID=identity-swagger` e `KC_ADMIN_CLIENT_ID=identity-admin`. `APP_ID=identity-service` identifica il servizio, ma non genera nomi Keycloak. `KC_ADMIN_CLIENT_SECRET` e' condiviso tra l'import del realm e Identity. I redirect del client Swagger sono derivati da `APPLICATION_URL`, senza slash finale. Per un frontend separato impostare anche `FRONTEND_URL` e aggiungere la sua origine a `CORS_ALLOWED_ORIGINS`. L'import non sovrascrive un realm esistente: usare il comando di setup descritto in [../keycloak/README.md](../keycloak/README.md).

`APPLICATION_ISSUER_NGROK` e' opzionale: valori vuoti e duplicati vengono ignorati dal resolver multi-issuer.

Verifica della configurazione senza avviare container:

```powershell
docker compose --env-file services/identity-service/.env.dev -f services/identity-service/compose.yml -f services/identity-service/compose.dev.yml config --quiet
docker compose --env-file services/identity-service/.env.prod -f services/identity-service/compose.yml -f services/identity-service/compose.prod.yml config --quiet
```

Per incorporare le librerie Identity in un altro microservizio, vedere [la guida del modulo](../../libs/common-keycloak/README.md).

## Pubblicazione delle librerie aggiornate

Identity Service richiede `platform.version=1.1.0`, che include `IdentityProperties`. La precedente 1.0.0 pubblicata non contiene questa classe. Dalla radice, pubblicare il parent e tutti i moduli con:

```powershell
./mvn-deploy.cmd
```

Solo dopo la pubblicazione riuscita, ricostruire l'immagine Identity Service. `mvn install` rende disponibili gli artefatti soltanto nella cache Maven locale, che il builder Docker non condivide. Non sovrascrivere una release gia' pubblicata con sorgenti diversi.
