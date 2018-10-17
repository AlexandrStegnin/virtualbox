package ru.stegnin.virtualbox.api.repository;

import ru.stegnin.virtualbox.api.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findOne(String id);

    User create(User user);

    User update(User user);

    User findByLogin(String login);

    User delete(String userId);

    boolean isLoginUnique(String login);

    boolean isEmailUnique(String email);

}
