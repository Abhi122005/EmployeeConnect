package com.example.edms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * This method sets the default view when the user visits the root URL ("/").
     * It maps the root path to the animation.html file.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // This line makes animation.html the very first page of the application.
        registry.addViewController("/").setViewName("forward:/animation.html");
    }
}
