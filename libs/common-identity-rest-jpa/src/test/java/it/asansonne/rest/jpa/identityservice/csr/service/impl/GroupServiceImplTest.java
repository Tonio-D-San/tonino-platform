package it.asansonne.rest.jpa.identityservice.csr.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.asansonne.common.core.exception.custom.NotFoundException;
import it.asansonne.rest.jpa.identityservice.csr.repository.GroupRepository;
import it.asansonne.rest.jpa.identityservice.csr.repository.specification.GroupSpecifications;
import it.asansonne.rest.jpa.identityservice.model.GroupModel;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GroupServiceImplTest {
  private final GroupRepository repository = mock(GroupRepository.class);
  private final GroupServiceImpl service = new GroupServiceImpl(repository, new GroupSpecifications());

  @Test
  void findsExistingGroupUsingTheRequestedRole() {
    var group = GroupModel.builder().role("string").build();
    when(repository.findByRole("string")).thenReturn(Optional.of(group));

    assertThat(service.findByRole(null, "string")).isSameAs(group);
  }

  @Test
  void findsExistingGroupUsingTheRequestedPathAndDescription() {
    var group = GroupModel.builder().role("string").build();
    when(repository.findByPath("/string")).thenReturn(Optional.of(group));
    when(repository.findByDescription("Example group")).thenReturn(Optional.of(group));

    assertThat(service.findByPath(null, "/string")).isSameAs(group);
    assertThat(service.findByDescription(null, "Example group")).isSameAs(group);
  }

  @Test
  void rejectsRolesThatDoNotExist() {
    when(repository.findByRole("missing")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.findByRole(null, "missing"))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("error.identity.group.not.found");
  }
}
