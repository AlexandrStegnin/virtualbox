package ru.stegnin.virtualbox.api.repository;

import ru.stegnin.virtualbox.api.model.Role;

import java.util.List;

public interface RoleRepository {
    List<Role> findAll();

    Role findOne(String id);

    void remove(Role role);

    Role create(Role role);

    Role update(Role role);

    Role findByRole(String name);

    Role remove(String roleId);
}
