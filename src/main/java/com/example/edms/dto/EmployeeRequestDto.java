package com.example.edms.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeRequestDto {

    @NotBlank
    @Size(max = 50)
    private String employeeId;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Size(max = 100)
    private String role;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal salary;

    @NotNull
    private LocalDate joiningDate;

    private LocalDate relievingDate;

    @NotNull
    private Boolean currentlyWorking;

    @NotNull
    @Min(0)
    private Integer experienceYears;

    private Long departmentId;

    public EmployeeRequestDto() {
    }

    public EmployeeRequestDto(String employeeId, String firstName, String lastName, String role, BigDecimal salary, LocalDate joiningDate, LocalDate relievingDate, Boolean currentlyWorking, Integer experienceYears, Long departmentId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.relievingDate = relievingDate;
        this.currentlyWorking = currentlyWorking;
        this.experienceYears = experienceYears;
        this.departmentId = departmentId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

    public String getLastName() {
        return lastName;
    }

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public String getRole() {
        return role;
    }

	public void setRole(String role) {
		this.role = role;
	}

    public BigDecimal getSalary() {
        return salary;
    }

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

    public LocalDate getRelievingDate() {
        return relievingDate;
    }

	public void setRelievingDate(LocalDate relievingDate) {
		this.relievingDate = relievingDate;
	}

    public Boolean getCurrentlyWorking() {
        return currentlyWorking;
    }

	public void setCurrentlyWorking(Boolean currentlyWorking) {
		this.currentlyWorking = currentlyWorking;
	}

    public Integer getExperienceYears() {
        return experienceYears;
    }

	public void setExperienceYears(Integer experienceYears) {
		this.experienceYears = experienceYears;
	}

    public Long getDepartmentId() {
        return departmentId;
    }

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
}
