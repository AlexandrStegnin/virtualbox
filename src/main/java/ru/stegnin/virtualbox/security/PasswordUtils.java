package ru.stegnin.virtualbox.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    public static String digestPassword(String plainTextPassword) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.encode(plainTextPassword);
        } catch (Exception e) {
            throw new RuntimeException("Exception encoding password", e);
        }
    }
}
