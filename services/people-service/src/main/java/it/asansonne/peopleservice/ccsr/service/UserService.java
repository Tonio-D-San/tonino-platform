package it.asansonne.peopleservice.ccsr.service;

import it.asansonne.common.jpa.service.CreateService;
import it.asansonne.common.jpa.service.DeleteService;
import it.asansonne.common.jpa.service.FindService;
import it.asansonne.common.jpa.service.UpdateService;
import it.asansonne.peopleservice.dto.input.FilterUser;
import it.asansonne.peopleservice.model.UserModel;
import java.security.Principal;

/**
 * The interface User service.
 */
public interface UserService extends
    CreateService<UserModel>, DeleteService, FindService<UserModel, FilterUser>, UpdateService<UserModel> {

  UserModel userByEmail(Principal principal, String email);

}
