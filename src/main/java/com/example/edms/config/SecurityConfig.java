package com.example.edms.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider(
            @Qualifier("adminDetailsService") UserDetailsService adminDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider employeeAuthenticationProvider(
            @Qualifier("employeeDetailsService") UserDetailsService employeeDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(employeeDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http, DaoAuthenticationProvider adminAuthenticationProvider) throws Exception {
        http
                .securityMatcher(
                        "/admin-login.html", "/forgot-password.html", "/reset-password.html",
                        "/admin-portal.html/**", "/api/admins/**", "/api/dashboard/**", "/api/departments/**",
                        "/css/**", "/js/**"
                )
                .authenticationProvider(adminAuthenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/admins/login", "/admin-login.html",
                                "/api/admins/forgot-password", "/forgot-password.html",
                                "/api/admins/reset-password", "/reset-password.html",
                                "/css/**", "/js/**"
                        ).permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin-login.html")
                        .loginProcessingUrl("/api/admins/login")
                        .defaultSuccessUrl("/admin-portal.html#dashboard", true)
                        .failureUrl("/admin-login.html?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/admin-login.html")
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appFilterChain(HttpSecurity http, DaoAuthenticationProvider employeeAuthenticationProvider) throws Exception {
        http
                .authenticationProvider(employeeAuthenticationProvider)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/animation.html", "/index.html", "/employee-login.html",
                                "/employee-forgot-password.html", "/employee-reset-password.html",
                                "/employee-portal.html", "/css/**", "/js/**", "/api/employees/**",
                                "/generate-hash").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/employee-login.html")
                        .loginProcessingUrl("/api/employees/login")
                        .defaultSuccessUrl("/employee-portal.html", true)
                        .failureUrl("/employee-login.html?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                );
        return http.build();
    }
}