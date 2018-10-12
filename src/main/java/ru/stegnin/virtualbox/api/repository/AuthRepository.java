package ru.stegnin.virtualbox.api.repository;

import org.springframework.stereotype.Repository;

import javax.jcr.RepositoryException;
import java.net.MalformedURLException;
import java.net.URI;

@Repository
public interface AuthRepository {
    URI getUri(Class cl, String methodName, Object... args);

    boolean openSession(String login, String password) throws MalformedURLException, RepositoryException;

    boolean closeSession();
}
