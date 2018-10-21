package ru.stegnin.virtualbox.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.stegnin.virtualbox.api.repository.AbstractRepository;
import ru.stegnin.virtualbox.api.repository.AuthRepository;
import ru.stegnin.virtualbox.server.repository.ServerRepository;

import javax.jcr.RepositoryException;
import java.net.MalformedURLException;
import java.net.URI;

@Service
public class AuthService extends AbstractRepository implements AuthRepository {

    private final ServerRepository serverRepository;
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    public AuthService(ServerRepository serverRepository, DaoAuthenticationProvider daoAuthenticationProvider) {
        this.serverRepository = serverRepository;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
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

    @Override
    public Authentication authenticate(String login, String password) {
        Authentication auth = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authentication = daoAuthenticationProvider.authenticate(auth);
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }
        return authentication;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
