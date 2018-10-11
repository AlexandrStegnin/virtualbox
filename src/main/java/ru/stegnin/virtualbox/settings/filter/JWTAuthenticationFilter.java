package ru.stegnin.virtualbox.settings.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ru.stegnin.virtualbox.api.model.User;
import ru.stegnin.virtualbox.api.service.AuthService;
import ru.stegnin.virtualbox.settings.security.KeyGenerator;
import ru.stegnin.virtualbox.settings.security.SimpleKeyGenerator;
import ru.stegnin.virtualbox.settings.support.Constants;

import javax.jcr.RepositoryException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;


public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private AuthenticationManager authenticationManager;
    private KeyGenerator keyGenerator;
    private AuthService authService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthService authService) {
        super("/*");
        this.authenticationManager = authenticationManager;
        this.keyGenerator = new SimpleKeyGenerator();
        this.authService = authService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User authUser = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authUser.getLogin(),
                            authUser.getPassword(),
                            authUser.getRoles()));
            if (auth.isAuthenticated()) {
                if (authService.openSession(authUser.getLogin(), authUser.getPassword())) {
                    System.out.printf("User with login '%s' authorized successful, session is open", authUser.getLogin());
                }
            }
            return auth;
        } catch (MismatchedInputException ex) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.addHeader(Constants.ERROR, "User unauthorized!");
        } catch (IOException | RepositoryException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        final Key key = keyGenerator.generateKey(Constants.SECRET_KEY);
        final String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        final String token = Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, key)
                .claim(Constants.AUTHORITIES_KEY, authorities)
                .compact();
        res.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + token);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}
