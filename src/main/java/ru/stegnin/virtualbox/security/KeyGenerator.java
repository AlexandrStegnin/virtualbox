package ru.stegnin.virtualbox.security;

import java.security.Key;

public interface KeyGenerator {
    Key generateKey(String keyString);
}
