package ru.stegnin.virtualbox.api.repository;

import org.springframework.security.core.Authentication;

import javax.jcr.RepositoryException;
import java.net.MalformedURLException;
import java.net.URI;

public interface AuthRepository {
    URI getUri(Class cl, String methodName, Object... args);

    boolean openSession(String login, String password) throws MalformedURLException, RepositoryException;

    boolean closeSession();

    Authentication authenticate(String login, String password);

    void logout();
}
