package ru.stegnin.virtualbox.server.service;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.springframework.stereotype.Service;
import ru.stegnin.virtualbox.server.repository.AbstractServerRepository;
import ru.stegnin.virtualbox.server.repository.ServerRepository;
import ru.stegnin.virtualbox.settings.support.Constants;

import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import java.net.MalformedURLException;

@Service
public class ServerRemoteService extends AbstractServerRepository implements ServerRepository {

    @Override
    public boolean openSession(String login, String password) throws RepositoryException, MalformedURLException {
        repository = new URLRemoteRepository(Constants.SERVER_URL);
        session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        return true;
    }

    @Override
    public boolean closeSession() {
        session.logout();
        return true;
    }
}
