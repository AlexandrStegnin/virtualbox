package ru.stegnin.virtualbox.api.repository;

import org.springframework.stereotype.Repository;
import ru.stegnin.virtualbox.api.model.Role;

import java.util.List;

@Repository
public interface RoleRepository {
    List<Role> findAll();

    Role findOne(String id);

    void remove(Role role);

    Role create(Role role);

    Role update(Role role);

    Role findByRole(String name);

    void init(String name);

    Role remove(String roleId);
}
