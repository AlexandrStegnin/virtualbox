package ru.stegnin.virtualbox.api.repository;

import org.jetbrains.annotations.Nullable;
import ru.stegnin.virtualbox.api.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    List<User> findAllByLogin(String login);

    User findOne(String id);

    void delete(User user);

    void create(User user);

    User update(User user);

    User findByLogin(String login);

    @Nullable User findByEmail(String email);

    void delete(String userId);

    boolean isLoginUnique(String login);

    boolean isEmailUnique(String email);

    List<User> saveAll(List<User> users);
}
