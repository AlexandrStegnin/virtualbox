package ru.stegnin.virtualbox.api.service;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.stegnin.virtualbox.api.model.Role;
import ru.stegnin.virtualbox.api.model.Role_;
import ru.stegnin.virtualbox.api.repository.AbstractRepository;
import ru.stegnin.virtualbox.api.repository.RoleRepository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class RoleService extends AbstractRepository implements RoleRepository {
    @Override
    public List<Role> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> from = cq.from(Role.class);
        cq.select(from);
        TypedQuery<Role> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Role findOne(String id) {
        return em.find(Role.class, id);
    }

    @Override
    public void remove(Role role) {
        em.remove(em.find(Role.class, role.getId()));
    }

    @Override
    public Role create(Role role) {
        return em.merge(role);
    }

    @Override
    public Role update(Role role) {
        Role oldRole = em.find(Role.class, role.getId());
        if (role.getName().isEmpty()) {
            role.setName(oldRole.getName());
        }
        return em.merge(role);
    }

    @Override
    public Role findByRole(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = builder.createQuery(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        Predicate predicate = builder.equal(root.get(Role_.name), name);
        criteriaQuery.where(predicate);
        criteriaQuery.select(root);
        TypedQuery<Role> query = em.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    @Override
    public void init(String name) {
        Role role = new Role.Builder().withName(name).build();
        create(role);
    }

    @Nullable
    @Override
    public Role remove(String roleId) {
        Role role = em.find(Role.class, roleId);
        if (role != null) {
            em.remove(role);
            return role;
        } else {
            return null;
        }
    }
}
