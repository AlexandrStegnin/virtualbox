package ru.stegnin.virtualbox.api.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stegnin.virtualbox.api.model.User;
import ru.stegnin.virtualbox.api.model.User_;
import ru.stegnin.virtualbox.api.repository.AbstractRepository;
import ru.stegnin.virtualbox.api.repository.UserRepository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService extends AbstractRepository implements UserRepository {
    private Logger logger = Logger.getLogger(UserService.class.getName());

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public List<User> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> from = cq.from(User.class);
        cq.select(from);
        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public List<User> findAllByLogin(String login) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.where(cb.like(root.get(User_.LOGIN), login));
        cq.select(root);
        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public User findOne(String userId) {
        return em.find(User.class, userId);
    }

    @Override
    public void delete(User user) {
        em.remove(findOne(user.getId()));
    }

    @Override
    public void create(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        em.persist(user);
    }

    @Override
    public User update(User user) {
        User oldUser = em.find(User.class, user.getId());
        if (user.getLogin().isEmpty()) {
            user.setLogin(oldUser.getLogin());
        }
        if (user.getEmail().isEmpty()) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getRole() == null) {
            user.setRole(oldUser.getRole());
        }
        if (oldUser.isEnabled()) {
            user.setEnabled(true);
        }
        return em.merge(user);
    }

    @Nullable
    @Override
    public User findByLogin(@NotNull String login) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = builder.equal(root.get(User_.LOGIN), login);
        criteriaQuery.where(predicate);
        criteriaQuery.select(root);
        TypedQuery<User> query = em.createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    @Override
    public @Nullable User findByEmail(@Nullable String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        Predicate predicate = cb.like(root.get(User_.email), email);
        cq.where(predicate);
        TypedQuery<User> query = em.createQuery(cq);
        return query.getSingleResult();
    }

    @Override
    public boolean isLoginUnique(@Nullable final String login) {
        if (login == null || login.isEmpty()) return false;
        return countByCondition(login, null) == 0;
    }

    @Override
    public boolean isEmailUnique(@Nullable final String email) {
        if (email == null || email.isEmpty()) return false;
        return countByCondition(null, email) == 0;
    }

    @NotNull
    private Long countByCondition(@Nullable String login, @Nullable String email) {
        List<Predicate> predicates = new ArrayList<>(2);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);
        if (login != null && !login.isEmpty()) {
            predicates.add(builder.like(root.get(User_.login), login));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(builder.like(root.get(User_.email), email));
        }
        criteriaQuery.select(builder.count(root));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public void delete(@NotNull String userId) {
        delete(em.find(User.class, userId));
    }

    @Override
    public List<User> saveAll(List<User> users) {
        users.forEach(u -> em.merge(u));
        return findAll();
    }
}
