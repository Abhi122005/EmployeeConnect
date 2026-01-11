package com.example.edms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id"})
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="employee_id", nullable = false, unique = true, length = 50)
    private String employeeId;

    /**
     * THIS IS THE FIX:
     * Replaced @JsonIgnore with @JsonProperty(access = JsonProperty.Access.WRITE_ONLY).
     * This allows the password to be received from the "Add Employee" form but ensures
     * it is never sent back out in any API response, fixing the creation error securely.
     */
    @Column(length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name="first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name="last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name="full_name", nullable = false, length = 201)
    private String fullName;

    @Column(name="role", nullable = false, length = 100)
    private String role;

    @Column(name="salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name="joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(name="relieving_date")
    private LocalDate relievingDate;

    @Column(name="currently_working", nullable = false)
    private Boolean currentlyWorking;

    @Column(name="experience_years", nullable = false)
    private Integer experienceYears;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference("employee-department")
    private Department department;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @PrePersist
    public void prePersist() {
        if (this.fullName == null || this.fullName.isBlank()) {
            setFullNameFromParts();
        }
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        setFullNameFromParts();
        this.updatedAt = Instant.now();
    }

    private void setFullNameFromParts() {
        String f = this.firstName != null ? this.firstName : "";
        String l = this.lastName != null ? this.lastName : "";
        this.fullName = (f + " " + l).trim();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public LocalDate getRelievingDate() { return relievingDate; }
    public void setRelievingDate(LocalDate relievingDate) { this.relievingDate = relievingDate; }
    public Boolean getCurrentlyWorking() { return currentlyWorking; }
    public void setCurrentlyWorking(Boolean currentlyWorking) { this.currentlyWorking = currentlyWorking; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl;}
}






