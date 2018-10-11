package ru.stegnin.virtualbox.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.stegnin.virtualbox.api.repository.AuthRepository;
import ru.stegnin.virtualbox.api.repository.AbstractRepository;
import ru.stegnin.virtualbox.server.ServerRepository;

import javax.jcr.RepositoryException;
import java.net.MalformedURLException;
import java.net.URI;

@Service
public class AuthService extends AbstractRepository implements AuthRepository {

    private final ServerRepository serverRepository;

    @Autowired
    public AuthService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public URI getUri(Class cl, String methodName, Object... args) {
        return MvcUriComponentsBuilder
                .fromMethodName(cl, methodName, args)
                .buildAndExpand()
                .toUri();
    }

    @Override
    public boolean openSession(String login, String password) throws MalformedURLException, RepositoryException {
        return serverRepository.openSession(login, password);
    }

    @Override
    public boolean closeSession() {
        return serverRepository.closeSession();
    }
}
