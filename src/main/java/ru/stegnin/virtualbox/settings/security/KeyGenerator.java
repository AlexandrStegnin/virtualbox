package ru.stegnin.virtualbox.settings.security;

import java.security.Key;

public interface KeyGenerator {
    Key generateKey(String keyString);
}
