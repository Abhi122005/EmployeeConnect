package com.example.edms.dto;
import lombok.Data;
import java.time.Instant;
@Data
public class DepartmentResponseDto {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private int employeeCount;
    public DepartmentResponseDto() {
    }

    public DepartmentResponseDto(Long id, String name, String code, String description, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

	public String getCode() {
		return code;
	}

    public String getDescription() {
        return description;
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

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
