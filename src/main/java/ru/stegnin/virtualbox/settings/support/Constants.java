package ru.stegnin.virtualbox.settings.support;

public class Constants {
    //    API Main

    public static final String API = "/api";
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String SERVER_URL = "http://localhost:8282/rmi";
    public static final String LOGOUT_URL = "/logout";
    public static final String MESSAGE_LOGIN_SUCCESSFUL = "User log in successful, session opened.";

    //    API для работы с User
    public static final String API_USERS = "/users";
    public static final String API_USER_ID = "userId";
    public static final String API_USERS_USER_ID = "/{userId}";
    public static final String API_AUTH_URL = API + API_USERS + "/auth";

    //    API для работы с Role
    public static final String API_ROLES = "/roles";
    public static final String API_ROLE_ID = "roleId";
    public static final String API_ROLES_ROLE_ID = "/{roleId}";

    //    Security Constants
    public static final String ERROR_MESSAGE = "ERROR Message";
    public static final String SUCCESS_MESSAGE = "SUCCESS Message";
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";

    //    Для работы с репозиторием JackRabbit
    public static final String API_JR_WORKSPACE = "/workspace";
    public static final String API_JR_FOLDERS_FOLDER_NAME = "/{folderName}";
    public static final String API_JR_FOLDER_NAME = "folderName";
    public static final String JR_NT_FOLDER = "nt:folder";
    public static final String JR_NT_FILE = "nt:file";


}
