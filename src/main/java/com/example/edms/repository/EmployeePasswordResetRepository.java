package com.example.edms.repository;

import com.example.edms.entity.EmployeePasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for EmployeePasswordReset entities.
 * This allows the application to perform CRUD operations on the
 * employee_password_resets table.
 */
@Repository
public interface EmployeePasswordResetRepository extends JpaRepository<EmployeePasswordReset, Long> {

    /**
     * Finds a password reset record by its unique token.
     * This is the key method used to verify a token during the reset process.
     * @param token The unique token string.
     * @return An Optional containing the EmployeePasswordReset if found.
     */
    Optional<EmployeePasswordReset> findByToken(String token);
}