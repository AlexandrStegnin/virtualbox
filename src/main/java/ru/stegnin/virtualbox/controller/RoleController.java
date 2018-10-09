package ru.stegnin.virtualbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stegnin.virtualbox.model.Role;
import ru.stegnin.virtualbox.repository.AuthRepository;
import ru.stegnin.virtualbox.repository.RoleRepository;
import ru.stegnin.virtualbox.support.Constants;
import ru.stegnin.virtualbox.support.GenericResponse;

import javax.servlet.ServletContext;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Найти роль по id
     * @param roleId - id пользователя
     * @return - response с сообщением
     */
    @GetMapping(value = Constants.API_ROLES_ROLE_ID)
    public ResponseEntity findById(@PathVariable(Constants.API_ROLE_ID) String roleId) {
        Role role = roleRepo.findOne(roleId);

        if (role == null) {
            message = new GenericResponse.Builder().withMessage("Role with id : " + roleId + " not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(role);
    }

    /**
     * Достать все роли
     * @return - response со списком пользователей
     */
    @GetMapping
    public ResponseEntity findAllRoles() {
        List<Role> allRoles = new ArrayList<>(roleRepo.findAll());
        return ResponseEntity.ok(allRoles);
    }

    /**
     * Изменить роль
     * @param roleId - id роли
     * @param role - данные роли для изменения в формате json
     * @return - response с сообщением
     */
    @PutMapping(value = Constants.API_ROLES_ROLE_ID)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable(Constants.API_ROLE_ID) String roleId, @RequestBody Role role) {
        role.setId(roleId);
        role = roleRepo.update(role);
        if (role != null) {
            return ResponseEntity.ok().body(role);
        } else {
            message = new GenericResponse.Builder().withMessage("Role with id " + roleId + " not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    /**
     * Удалить роль по id
     * @param roleId - id роли
     * @return - response с сообщением
     */
    @DeleteMapping(value = Constants.API_ROLES_ROLE_ID)
    public ResponseEntity remove(@PathVariable(Constants.API_ROLE_ID) String roleId) {
        Role role = roleRepo.remove(roleId);
        if (role != null) {
            message = new GenericResponse.Builder().withMessage("Role with id " + roleId + " deleted successful.").build();
            return ResponseEntity.ok().body(message);
        } else {
            message = new GenericResponse.Builder().withMessage("Role with id " + roleId + " not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

}
