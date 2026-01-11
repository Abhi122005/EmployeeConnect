package com.example.edms.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A safe Data Transfer Object for sending admin details to the frontend.
 * This object is "flat" and has no circular references, which prevents server crashes.
 */
@Data
public class AdminResponseDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private BigDecimal salary;
    private LocalDate joiningDate;
    private String departmentName; // We only need the name, not the whole object
    private String profileImageUrl;
}
