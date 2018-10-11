package ru.stegnin.virtualbox.api.repository;

import java.net.URI;

public interface AuthRepository {
    URI getUri(Class cl, String methodName, Object... args);
}
