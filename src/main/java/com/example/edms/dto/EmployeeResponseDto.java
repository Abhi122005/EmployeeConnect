package com.example.edms.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
@Data
public class EmployeeResponseDto {

    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String role;
    private BigDecimal salary;
    private LocalDate joiningDate;
    private LocalDate relievingDate;
    private Boolean currentlyWorking;
    private Integer experienceYears;
    private Long departmentId;
    private String departmentName;
    private Instant createdAt;
    private Instant updatedAt;
    private DepartmentResponseDto department;
    private String profileImageUrl;
    public EmployeeResponseDto() {
    }

    public EmployeeResponseDto(Long id, String employeeId, String firstName, String lastName, String fullName, String role, BigDecimal salary, LocalDate joiningDate, LocalDate relievingDate, Boolean currentlyWorking, Integer experienceYears, DepartmentResponseDto department, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.relievingDate = relievingDate;
        this.currentlyWorking = currentlyWorking;
        this.experienceYears = experienceYears;
        this.department = department;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public java.math.BigDecimal getSalary() {
        return salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public LocalDate getRelievingDate() {
        return relievingDate;
    }

    public Boolean getCurrentlyWorking() {
        return currentlyWorking;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public DepartmentResponseDto getDepartment() {
        return department;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSalary(java.math.BigDecimal salary) {
        this.salary = salary;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setRelievingDate(LocalDate relievingDate) {
        this.relievingDate = relievingDate;
    }

    public void setCurrentlyWorking(Boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setDepartment(DepartmentResponseDto department) {
        this.department = department;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
