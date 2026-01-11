package com.example.edms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "employee123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println();
        System.out.println("BCrypt hash for '" + rawPassword + "':");
        System.out.println(encodedPassword);
        System.out.println();
    }
}
