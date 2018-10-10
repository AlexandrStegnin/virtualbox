package ru.stegnin.virtualbox.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.stegnin.virtualbox.model.User;
import ru.stegnin.virtualbox.repository.AbstractRepository;
import ru.stegnin.virtualbox.repository.AuthRepository;
import ru.stegnin.virtualbox.repository.UserRepository;
import ru.stegnin.virtualbox.support.Constants;

import javax.servlet.ServletContext;
import java.net.URI;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthService extends AbstractRepository implements AuthRepository {

    private final UserRepository userRepo;

    @Autowired
    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public String generateToken(String login, ServletContext context) {
        Key key = keyGenerator.generateKey(Constants.SECRET_KEY);
        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setIssuer(context.getContextPath())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(15L).atZone(ZoneId.systemDefault()).toInstant()))
                // FIXME: 10.10.2018 Добавить Claims authorities
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        LOGGER.info("#### generating token for a key : " + jwtToken + " - " + key);
        return jwtToken;
    }

    @Override
    public boolean check(@NotNull final String login, @NotNull final String password) {
        if (login.isEmpty()) return false;
        if (password.isEmpty()) return false;
        final User user = userRepo.findByLogin(login);
//        String digestPassword = PasswordUtils.digestPassword(password);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public URI getUri(Class cl, String methodName, Object... args) {
        return MvcUriComponentsBuilder
                .fromMethodName(cl, methodName, args)
                .buildAndExpand()
                .toUri();
    }
}
