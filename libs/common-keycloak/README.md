# People come modulo riutilizzabile

Un'applicazione puo' aggiungere `it.asansonne:common-people-rest-jpa:1.1.0` alle dipendenze Maven. Il modulo include `common-keycloak` e registra componenti, servizi, entita' e repository tramite le autoconfigurazioni esistenti. Le nuove proprieta' vengono caricate dalla libreria, senza copiare l'application.properties del servizio People.

Configurazione minima per l'integrazione Keycloak:

```properties
people.app-id=gestionale
people.keycloak.base-url=https://auth.example.com
people.keycloak.admin-client-secret=${PEOPLE_ADMIN_SECRET}
```

I default sono realm `gestionale`, client API `gestionale-api`, backend `gestionale-be`, frontend `gestionale-fe`. L'identificativo deve iniziare con una lettera minuscola e contenere solo lettere minuscole, cifre e trattini. URL e secret sono obbligatori. La configurazione viene verificata all'avvio.

Per un realm esistente si possono sovrascrivere singolarmente:

```properties
people.keycloak.realm=realm-condiviso
people.keycloak.api-client-id=api-esistente
people.keycloak.admin-client-id=service-account-esistente
people.keycloak.frontend-client-id=frontend-esistente
```

| Proprieta' | Variabile usata dal Compose e dai default della libreria |
| --- | --- |
| people.app-id | APP_ID |
| people.keycloak.base-url | KEYCLOAK_URL |
| people.keycloak.realm | KEYCLOAK_REALM_NAME |
| people.keycloak.api-client-id | KEYCLOAK_CLIENT_ID |
| people.keycloak.admin-client-id | KC_ADMIN_CLIENT_ID |
| people.keycloak.frontend-client-id | KEYCLOAK_APP_CLIENT_ID |
| people.keycloak.admin-client-secret | PLATFORM_BACKEND_CLIENT_SECRET |

Le proprieta' esplicite dell'applicazione prevalgono sui default della libreria. I nomi `keycloak.host.*`, `keycloak.client.id` e `keycloak.admin.*` non sono piu' letti dai componenti aggiornati: migrare al namespace `people.*`. Gli env gia' usati dal Compose restano supportati.

L'app ospitante deve comunque configurare il datasource e il proprio schema, gli endpoint REST e la propria SecurityFilterChain. La libreria non impone i controller, Swagger o le regole di autorizzazione del servizio di esempio. Per l'autenticazione JWT usare `PeopleProperties.keycloak().realmUrl()` come issuer; i ruoli API sono sotto `resource_access[apiClientId]`.

Il setup Keycloak e' separato dal runtime: seguire [la guida di provisioning](../../services/keycloak/README.md). Il backend riceve `realm-management/manage-users` nel realm dell'app; non riceve credenziali di amministrazione del server. Le autorizzazioni dei chiamanti alle operazioni sulle persone restano responsabilita' dell'app ospitante.

Ogni contesto applicativo usa un solo realm. Per piu' applicazioni indipendenti configurare istanze/datasource separati. Il solo cambio di APP_ID non separa le tabelle di un database condiviso e non introduce un sistema multi-tenant.
