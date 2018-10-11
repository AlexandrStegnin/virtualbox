package ru.stegnin.virtualbox.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.stegnin.virtualbox.api.model.User;
import ru.stegnin.virtualbox.api.repository.AuthRepository;
import ru.stegnin.virtualbox.api.repository.UserRepository;
import ru.stegnin.virtualbox.settings.support.Constants;
import ru.stegnin.virtualbox.settings.support.GenericResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(Constants.API + Constants.API_USERS)
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class.getName());
    private GenericResponse message;

    private final UserRepository userRepo;
    private final AuthRepository auth;

    @Autowired
    public UserController(UserRepository userRepo, AuthRepository auth) {
        this.userRepo = userRepo;
        this.auth = auth;
    }

    /**
     * Создать пользователя
     *
     * @param user - пользователь в формате json
     * @return - response с сообщением
     */
    @PostMapping
    public ResponseEntity
    create(@RequestBody User user) {
        user = userRepo.create(user);
        message = new GenericResponse.Builder().withMessage("User : " + user + " created").build();
        logger.info(message.getMessage());
        return ResponseEntity.created(auth.getUri(this.getClass(), "create", (Object) user)).body(user);
    }

    /**
     * Найти пользователя по id
     *
     * @param userId - id пользователя
     * @return - response с сообщением
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = Constants.API_USERS_USER_ID)
    public ResponseEntity findById(@PathVariable(Constants.API_USER_ID) String userId) {
        User user = userRepo.findOne(userId);
        if (user == null) {
            message = new GenericResponse.Builder().withError("User with id : " + userId + " not found").build();
            logger.warning(message.getError());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        logger.info(message.getMessage());
        return ResponseEntity.ok(user);
    }

    /**
     * Достать всех пользователей
     *
     * @return - response со списком пользователей
     */
    @GetMapping
    public ResponseEntity findAllUsers() {
        List<User> allUsers = new ArrayList<>(userRepo.findAll());
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Изменить пользователя
     *
     * @param userId - id пользователя
     * @param user   - данные пользователя для изменения в формате json
     * @return - response с сообщением
     */
    @PutMapping(value = Constants.API_USERS_USER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable(Constants.API_USER_ID) String userId, @RequestBody User user) {
        user.setId(userId);
        user = userRepo.update(user);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            message = new GenericResponse.Builder().withError("User with id " + userId + " not found.").build();
            logger.warning(message.getError());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    /**
     * Удалить пользователя по id
     *
     * @param userId - id пользователя
     * @return - response с сообщением
     */
    @DeleteMapping(value = Constants.API_USERS_USER_ID)
    public ResponseEntity remove(@PathVariable(Constants.API_USER_ID) String userId) {
        User user = userRepo.delete(userId);
        if (user != null) {
            message = new GenericResponse.Builder().withMessage("User with id " + userId + " deleted successful.").build();
            logger.info(message.getMessage());
            return ResponseEntity.ok().body(message);
        } else {
            message = new GenericResponse.Builder().withError("User with id " + userId + " not found.").build();
            logger.warning(message.getError());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    /**
     * выйти из системы
     *
     * @return - response с сообщением
     */
    @PostMapping(value = Constants.LOGOUT)
    public ResponseEntity logout() {
        SecurityContextHolder.clearContext();
        String msg = "successful.";
        if (!auth.closeSession()) msg = "failed.";
        message = new GenericResponse.Builder().withMessage("User logout " + msg).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
}
