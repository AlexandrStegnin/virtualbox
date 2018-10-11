package ru.stegnin.virtualbox.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.stegnin.virtualbox.api.repository.AuthRepository;
import ru.stegnin.virtualbox.api.repository.AbstractRepository;

import java.net.URI;

@Service
public class AuthService extends AbstractRepository implements AuthRepository {

    @Override
    public URI getUri(Class cl, String methodName, Object... args) {
        return MvcUriComponentsBuilder
                .fromMethodName(cl, methodName, args)
                .buildAndExpand()
                .toUri();
    }
}
