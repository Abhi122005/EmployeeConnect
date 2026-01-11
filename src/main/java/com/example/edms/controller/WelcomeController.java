package com.example.edms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    /**
     * Redirects the root URL "/" to the transition.html page.
     * This ensures the animated splash screen is the first page shown.
     * @return a redirect URL to the transition page.
     */
    @GetMapping("/")
    public String redirectToWelcomePage() {
        return "redirect:/animation.html";
    }
}