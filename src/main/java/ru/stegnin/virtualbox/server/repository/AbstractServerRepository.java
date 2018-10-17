package ru.stegnin.virtualbox.server.repository;

import org.springframework.web.context.annotation.ApplicationScope;

import javax.jcr.Repository;
import javax.jcr.Session;
@ApplicationScope
public abstract class AbstractServerRepository {
    protected Repository repository;
    protected Session session;
}
