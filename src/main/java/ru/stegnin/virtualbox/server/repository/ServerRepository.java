package ru.stegnin.virtualbox.server.repository;

import javax.jcr.RepositoryException;
import java.net.MalformedURLException;

public interface ServerRepository {
    boolean openSession(String login, String password) throws MalformedURLException, RepositoryException;
    boolean closeSession();
    // TODO: 11.10.2018 Synchronization добавить
}
