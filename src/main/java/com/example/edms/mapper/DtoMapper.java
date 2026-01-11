package com.example.edms.mapper;

import com.example.edms.dto.*;
import com.example.edms.entity.Admin;
import com.example.edms.entity.Department;
import com.example.edms.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    /**
     * This method safely converts an Admin entity to a safe AdminResponseDto.
     * It is the final piece of the fix that prevents the infinite loop and server crash.
     */
    public AdminResponseDto toAdminResponse(Admin admin) {
        if (admin == null) return null;
        AdminResponseDto dto = new AdminResponseDto();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setEmail(admin.getEmail());
        dto.setFullName(admin.getFullName());
        dto.setRole(admin.getRole());
        dto.setSalary(admin.getSalary());
        dto.setJoiningDate(admin.getJoiningDate());
        dto.setProfileImageUrl(admin.getProfileImageUrl());
        if (admin.getDepartment() != null) {
            dto.setDepartmentName(admin.getDepartment().getName());
        }
        return dto;
    }

    public DepartmentResponseDto toDepartmentResponse(Department departmentEntity) {
        if (departmentEntity == null) return null;
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setId(departmentEntity.getId());
        dto.setName(departmentEntity.getName());
        dto.setCode(departmentEntity.getCode());
        dto.setDescription(departmentEntity.getDescription());
        dto.setCreatedAt(departmentEntity.getCreatedAt());
        dto.setUpdatedAt(departmentEntity.getUpdatedAt());
        if (departmentEntity.getEmployees() != null) {
            dto.setEmployeeCount(departmentEntity.getEmployees().size());
        } else {
            dto.setEmployeeCount(0);
        }
        return dto;
    }

    public Department toDepartmentEntity(DepartmentRequestDto dto) {
        if (dto == null) return null;
        Department entity = new Department();
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public EmployeeResponseDto toEmployeeResponse(Employee employeeEntity) {
        if (employeeEntity == null) return null;
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employeeEntity.getId());
        dto.setEmployeeId(employeeEntity.getEmployeeId());
        dto.setFirstName(employeeEntity.getFirstName());
        dto.setLastName(employeeEntity.getLastName());
        dto.setFullName(employeeEntity.getFullName());
        dto.setRole(employeeEntity.getRole());
        dto.setSalary(employeeEntity.getSalary());
        dto.setJoiningDate(employeeEntity.getJoiningDate());
        dto.setRelievingDate(employeeEntity.getRelievingDate());
        dto.setCurrentlyWorking(employeeEntity.getCurrentlyWorking());
        dto.setExperienceYears(employeeEntity.getExperienceYears());
        dto.setCreatedAt(employeeEntity.getCreatedAt());
        dto.setUpdatedAt(employeeEntity.getUpdatedAt());
        dto.setProfileImageUrl(employeeEntity.getProfileImageUrl());
        if (employeeEntity.getDepartment() != null) {
            dto.setDepartmentId(employeeEntity.getDepartment().getId());
            dto.setDepartmentName(employeeEntity.getDepartment().getName());
        }
        return dto;
    }

    public Employee toEmployeeEntity(EmployeeRequestDto dto, Department department) {
        if (dto == null) return null;
        Employee entity = new Employee();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setRole(dto.getRole());
        entity.setSalary(dto.getSalary());
        entity.setJoiningDate(dto.getJoiningDate());
        entity.setRelievingDate(dto.getRelievingDate());
        entity.setCurrentlyWorking(dto.getCurrentlyWorking());
        entity.setExperienceYears(dto.getExperienceYears());
        entity.setDepartment(department);
        return entity;
    }

    public SalaryStatementDto toSalaryStatement(Employee employeeEntity) {
        if (employeeEntity == null) return null;
        SalaryStatementDto dto = new SalaryStatementDto();
        dto.setEmployeeId(employeeEntity.getEmployeeId());
        dto.setSalary(employeeEntity.getSalary());
        dto.setJoiningDate(employeeEntity.getJoiningDate());
        dto.setRelievingDate(employeeEntity.getRelievingDate());
        dto.setCurrentlyWorking(employeeEntity.getCurrentlyWorking());
        return dto;
    }
}
