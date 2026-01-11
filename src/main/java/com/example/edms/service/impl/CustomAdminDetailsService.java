package com.example.edms.service.impl;

import com.example.edms.entity.Admin;
import com.example.edms.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// ** THIS IS THE FIX **
// The @Service("adminDetailsService") annotation creates the bean
// that your SecurityConfig is looking for with the qualifier "adminDetailsService".
@Service("adminDetailsService")
public class CustomAdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));

        // Spring Security requires roles to start with "ROLE_"
        String role = "ROLE_" + admin.getRole().toUpperCase();

        return new User(admin.getUsername(), admin.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
