package ru.stegnin.virtualbox.repository;

import ru.stegnin.virtualbox.security.KeyGenerator;
import ru.stegnin.virtualbox.security.SimpleKeyGenerator;

import javax.servlet.ServletContext;
import java.net.URI;
import java.util.logging.Logger;

public interface AuthRepository {

    KeyGenerator keyGenerator = new SimpleKeyGenerator();
    Logger LOGGER = Logger.getLogger(AuthRepository.class.getName());

    String generateToken(String login, ServletContext context);

    boolean check(String login, String password);

    URI getUri(Class cl, String methodName, Object... args);
}
