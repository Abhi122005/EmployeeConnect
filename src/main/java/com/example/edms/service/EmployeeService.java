package com.example.edms.service;

import com.example.edms.dto.EmployeeRequestDto;
import com.example.edms.dto.EmployeeResponseDto;
import com.example.edms.dto.SalaryStatementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.edms.entity.Employee;
import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto create(com.example.edms.dto.EmployeeRequestDto dto);
    EmployeeResponseDto update(Long id, EmployeeRequestDto dto);
    void delete(Long id);
    void deleteMultiple(List<Long> ids);
    EmployeeResponseDto getById(Long id);
    EmployeeResponseDto getByEmployeeId(String employeeId);
    SalaryStatementDto getSalaryStatementByEmployeeId(String employeeId);
    Page<EmployeeResponseDto> search(String employeeId, String name, String role, String departmentCode, Pageable pageable);
    void assignDepartment(Long employeeId, Long departmentId);
    List<EmployeeResponseDto> getAll();
    List<SalaryStatementDto> getSalaryStatementHistory(String employeeId);
    String generateResetToken(String employeeId);
    boolean resetPassword(String token, String newPassword);
    Employee updateProfileImageUrl(Long id, String imageUrl);
}
