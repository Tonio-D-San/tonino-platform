package it.asansonne.rest.jpa.peopleservice.csr.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupSyncRunner {

  private final GroupComponent groupComponent;

  @Value("${people.groups.sync.on-startup:false}")
  private boolean syncOnStartup;
  @Value("${people.groups.sync.scheduled.enabled:false}")
  private boolean scheduledSyncEnabled;

  @EventListener(ApplicationReadyEvent.class)
  public void syncOnStartup() {
    if (syncOnStartup) {
      sync("startup");
    }
  }

  @Scheduled(fixedDelayString = "${people.groups.sync.fixed-delay-ms:3600000}")
  public void syncScheduled() {
    if (scheduledSyncEnabled) {
      sync("scheduled");
    }
  }

  private void sync(String trigger) {
    log.info("Avvio sincronizzazione gruppi Keycloak: trigger={}", trigger);
    groupComponent.syncFromKeycloak();
  }
}
