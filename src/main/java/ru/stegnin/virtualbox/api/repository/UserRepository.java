package ru.stegnin.virtualbox.api.repository;

import org.springframework.stereotype.Repository;
import ru.stegnin.virtualbox.api.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    List<User> findAll();

    User findOne(String id);

    void delete(User user);

    User create(User user);

    User update(User user);

    User findByLogin(String login);

    Optional<User> findByEmail(String email);

    void init(String login, String email, String password);

    User delete(String userId);

    boolean isLoginUnique(String login);

    boolean isEmailUnique(String email);

}
