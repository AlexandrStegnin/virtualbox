package ru.stegnin.virtualbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stegnin.virtualbox.model.User;
import ru.stegnin.virtualbox.repository.UserRepository;
import ru.stegnin.virtualbox.repository.AuthRepository;
import ru.stegnin.virtualbox.support.Constants;
import ru.stegnin.virtualbox.support.GenericResponse;

import javax.servlet.ServletContext;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(Constants.API + Constants.API_USERS)
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class.getName());
    private GenericResponse message;

    private final UserRepository userRepo;
    private final AuthRepository auth;
    private final ServletContext context;

    @Autowired
    public UserController(UserRepository userRepo, AuthRepository auth, ServletContext context) {
        this.userRepo = userRepo;
        this.auth = auth;
        this.context = context;
    }

    /**
     * Аутентификация пользователя по логину и паролю
     * @param user - данные пользователя (логин/пароль) в формате json
     * @return - response с сообщением
     */
    @PostMapping(value = Constants.API_USERS_AUTH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity
    authenticateUser(@RequestBody User user) {
        try {

            logger.info("#### try to login by ussr credentials ####");

            // Check user using the credentials provided
            boolean check = auth.check(user.getLogin(), user.getPassword());
            if (!check) throw new  SecurityException("login/password is incorrect");

            // Issue a token for the user
            String token = auth.generateToken(user.getLogin(), context);

            // Generate message body
            message = new GenericResponse.Builder().withMessage(Constants.MESSAGE_LOGIN_SUCCESSFUL).build();

            // Return the token on the response
            return ResponseEntity.ok().header(AUTHORIZATION, "Bearer " + token).body(message);

        } catch (Exception e) {
            message = new GenericResponse.Builder().withMessage(Constants.MESSAGE_LOGIN_UNAUTHORIZED).build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
    }

    /**
     * Создать пользователя
     * @param user - пользователь в формате json
     * @return - response с сообщением
     */
    @PostMapping
    public ResponseEntity
    create(@RequestBody User user) {
        user = userRepo.create(user);
        message = new GenericResponse.Builder().withMessage("User : " + user + " created").build();
        return ResponseEntity.created(auth.getUri(this.getClass(), "create", (Object) user)).body(user);
    }

    /**
     * Найти пользователя по id
     * @param userId - id пользователя
     * @return - response с сообщением
     */
    @GetMapping(value = Constants.API_USERS_USER_ID)
    public ResponseEntity findById(@PathVariable(Constants.API_USER_ID) String userId) {
        User user = userRepo.findOne(userId);

        if (user == null) {
            message = new GenericResponse.Builder().withMessage("User with id : " + userId + " not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Достать всех пользователей
     * @return - response со списком пользователей
     */
    @GetMapping
    public ResponseEntity findAllUsers() {
        List<User> allUsers = new ArrayList<>(userRepo.findAll());
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Изменить пользователя
     * @param userId - id пользователя
     * @param user - данные пользователя для изменения в формате json
     * @return - response с сообщением
     */
    @PutMapping(value = Constants.API_USERS_USER_ID)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable(Constants.API_USER_ID) String userId, @RequestBody User user) {
        user.setId(userId);
        user = userRepo.update(user);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            message = new GenericResponse.Builder().withMessage("User with id " + userId + " not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    /**
     * Удалить пользователя по id
     * @param userId - id пользователя
     * @return - response с сообщением
     */
    @DeleteMapping(value = Constants.API_USERS_USER_ID)
    public ResponseEntity remove(@PathVariable(Constants.API_USER_ID) String userId) {
        User user = userRepo.delete(userId);
        if (user != null) {
            message = new GenericResponse.Builder().withMessage("User with id " + userId + " deleted succesfull.").build();
            return ResponseEntity.ok().body(message);
        } else {
            message = new GenericResponse.Builder().withMessage("User with id " + userId + " not found.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
}