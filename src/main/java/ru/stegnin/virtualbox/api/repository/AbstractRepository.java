package ru.stegnin.virtualbox.api.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public abstract class AbstractRepository {

    @PersistenceContext
    public EntityManager em;

}
