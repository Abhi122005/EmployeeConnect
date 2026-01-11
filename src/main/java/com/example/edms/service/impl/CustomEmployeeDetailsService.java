package com.example.edms.service.impl;

import com.example.edms.entity.Employee;
import com.example.edms.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * This service is responsible for loading employee-specific user details from the database.
 * The @Service("employeeDetailsService") annotation creates the bean with the qualifier
 * that the SecurityConfig expects for handling employee authentication.
 */
@Service("employeeDetailsService")
public class CustomEmployeeDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // In our system, the "username" for an employee is their unique employeeId.
        Employee employee = employeeRepository.findByEmployeeId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with ID: " + username));

        // Assign a default role for all employees. Spring Security requires roles to start with "ROLE_".
        String role = "ROLE_EMPLOYEE";

        // Create a Spring Security User object with the employee's credentials and role.
        return new User(employee.getEmployeeId(), employee.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}