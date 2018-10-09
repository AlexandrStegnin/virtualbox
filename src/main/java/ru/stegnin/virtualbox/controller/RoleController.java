package ru.stegnin.virtualbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stegnin.virtualbox.model.Role;
import ru.stegnin.virtualbox.repository.AuthRepository;
import ru.stegnin.virtualbox.repository.RoleRepository;
import ru.stegnin.virtualbox.support.Constants;
import ru.stegnin.virtualbox.support.GenericResponse;

import javax.servlet.ServletContext;
import java.util.logging.Logger;

@RestController
@RequestMapping(Constants.API + Constants.API_ROLES)
public class RoleController {
    private Logger logger = Logger.getLogger(RoleController.class.getName());
    private GenericResponse message;

    private final RoleRepository roleRepo;
    private final AuthRepository auth;
    private final ServletContext context;

    @Autowired
    public RoleController(RoleRepository roleRepo, AuthRepository auth, ServletContext context) {
        this.roleRepo = roleRepo;
        this.auth = auth;
        this.context = context;
    }

    /**
     * Создать роль
     * @param role - роль в формате json
     * @return - response с сообщением
     */
    @PostMapping
    public ResponseEntity
    create(@RequestBody Role role) {
        role = roleRepo.create(role);
        message = new GenericResponse.Builder().withMessage("Role : " + role + " created").build();
        return ResponseEntity.created(auth.getUri(this.getClass(), "create", (Object) role)).body(role);
    }

}
