package com.example.edms.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class AdminUpdateRequestDto {
    private String fullName;
    private String username;
    private String email;
    private String password; // Will be ignored if null or empty
    private String role;
    private BigDecimal salary;
    private LocalDate joiningDate;
    private Integer experienceYears;
    private Long departmentId;
}