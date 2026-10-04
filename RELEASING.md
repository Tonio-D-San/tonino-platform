# Pubblicare le librerie

`mvn-deploy.cmd` esegue il reactor completo con test abilitati. Il parent configura `deployAtEnd=true`: gli upload iniziano soltanto quando tutti i moduli hanno completato la build. Questo evita una pubblicazione parziale dovuta a errori di compilazione o test nei moduli successivi.

La pubblicazione remota non e' atomica: un errore HTTP o di rete durante gli upload puo' ancora lasciare artefatti parzialmente pubblicati. Non c'e' rollback automatico. Un 409 al secondo tentativo puo' indicare che le stesse coordinate release sono gia' presenti; controllare l'artefatto indicato nel log.

## Rilasci

Scegliere una versione nuova per ogni release. Per esempio, se 1.1.0 e' gia' stata pubblicata anche solo parzialmente:

```powershell
./mvn-version.ps1 -Version 1.1.1 -WhatIf
./mvn-version.ps1 -Version 1.1.1
./mvn-deploy.cmd verify
./mvn-deploy.cmd deploy
```

Il primo script aggiorna parent, moduli e dipendenze interne, revision e platform.version del servizio Identity. Non cambia le versioni delle dipendenze esterne ne' la versione propria del servizio. Controllare il diff e conservare le modifiche nel controllo versione insieme ai sorgenti del rilascio.

`verify` compila ed esegue i test senza pubblicare. `deploy` esegue nuovamente la build completa e pubblica alla fine. Senza argomenti, `mvn-deploy.cmd` mantiene il comportamento precedente ed esegue `deploy`. JAVA_HOME e MAVEN_SETTINGS_FILE possono essere impostati dall'ambiente; altrimenti usa i percorsi locali gia' previsti dal progetto. Il codice di uscita Maven viene propagato al chiamante.

Se la release remota e' incompleta, la strategia ordinaria e' una nuova versione per tutto il reactor. Non rilanciare indiscriminatamente la stessa release e non usare -rf come rollback: gli artefatti gia' caricati rimangono sul server. La cancellazione di versioni remote richiede una scelta esplicita e non viene automatizzata da questi script.

## Sviluppo ripetuto

Per build di sviluppo pubblicabili ripetutamente usare una versione SNAPSHOT:

```powershell
./mvn-version.ps1 -Version 1.2.0-SNAPSHOT
./mvn-deploy.cmd deploy
```

GitHub Packages supporta snapshot Maven. I consumer devono abilitare gli snapshot nel repository e possono usare `mvn -U` per controllare gli aggiornamenti. People li abilita nel proprio POM; per una release definitiva selezionare una versione senza -SNAPSHOT. Non eseguire in parallelo due pubblicazioni della stessa versione snapshot.

La build Docker continua a scaricare le librerie pubblicate: dopo il deploy ricostruire People. Con snapshot, una build Docker riutilizzata dalla cache non controlla gli aggiornamenti: usare `docker compose ... build --no-cache people` quando necessario.

Riferimenti: [Maven deployAtEnd](https://maven.apache.org/plugins/maven-deploy-plugin/deploy-mojo.html), [GitHub Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).
