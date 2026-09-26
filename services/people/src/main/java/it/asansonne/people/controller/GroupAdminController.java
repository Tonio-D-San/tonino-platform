package it.asansonne.people.controller;

import it.asansonne.common.people.dto.response.Group;
import it.asansonne.rest.jpa.peopleservice.csr.component.GroupComponent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/admin${api.resource.groups}")
@RequiredArgsConstructor
public class GroupAdminController {

  private final GroupComponent groupComponent;

  @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Group> syncFromKeycloak() {
    return groupComponent.syncFromKeycloak();
  }
}
