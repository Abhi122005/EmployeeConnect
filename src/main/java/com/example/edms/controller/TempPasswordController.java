package com.example.edms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a temporary utility controller to generate password hashes.
 * IT SHOULD BE DELETED AFTER YOU HAVE USED IT.
 */
@RestController
public class TempPasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/generate-hash")
    public String generateHash(@RequestParam(defaultValue = "employee123") String password) {
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Generated hash for '" + password + "': " + encodedPassword);
        return "<h3>BCrypt hash for '" + password + "':</h3><p style='font-family:monospace;'>" + encodedPassword + "</p>";
    }
}