package com.example.edms.service;

import com.example.edms.dto.DepartmentRequestDto;
import com.example.edms.dto.DepartmentResponseDto;
import com.example.edms.dto.EmployeeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    DepartmentResponseDto create(DepartmentRequestDto dto);
    DepartmentResponseDto update(Long id, DepartmentRequestDto dto);
    void delete(Long id);
    DepartmentResponseDto getById(Long id);
    Page<DepartmentResponseDto> list(Pageable pageable);
    List<EmployeeResponseDto> listEmployees(Long departmentId);
    List<DepartmentResponseDto> getAll();
}
