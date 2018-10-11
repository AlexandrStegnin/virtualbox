package ru.stegnin.virtualbox.settings.support;

public class Constants {
    public static final String API = "/api";
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String SERVER_URL = "http://localhost:8282/rmi";
    public static final String LOGOUT = "/logout";

    //    API для работы с User
    public static final String API_USERS = "/users";
    public static final String API_USER_ID = "userId";
    public static final String API_USERS_USER_ID = "/{userId}";
    public static final String API_USERS_AUTH = "/auth";

    //    API для работы с Role
    public static final String API_ROLES = "/roles";
    public static final String API_ROLE_ID = "roleId";
    public static final String API_ROLES_ROLE_ID = "/{roleId}";

    //    MESSAGES FOR RESPONSE
    public static final String MESSAGE_LOGIN_SUCCESSFUL = "User log in successful!";
    public static final String MESSAGE_LOGIN_UNAUTHORIZED = "User not found!";

    //    Security Constants
    public static final String SECRET_KEY = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 900_000; // 15 minutes
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String API_AUTH_URL = "/api/users/auth";
    public static final String ERROR = "ERROR Message";

}
