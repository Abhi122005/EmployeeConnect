package com.example.edms.service.impl;

import com.example.edms.dto.DepartmentRequestDto;
import com.example.edms.dto.DepartmentResponseDto;
import com.example.edms.dto.EmployeeResponseDto;
import com.example.edms.entity.Department;
import com.example.edms.exception.ConflictException;
import com.example.edms.exception.ResourceNotFoundException;
import com.example.edms.mapper.DtoMapper;
import com.example.edms.repository.DepartmentRepository;
import com.example.edms.repository.EmployeeRepository;
import com.example.edms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DtoMapper mapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, DtoMapper mapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public DepartmentResponseDto create(DepartmentRequestDto dto) {
        if (departmentRepository.existsByCode(dto.getCode())) {
            throw new ConflictException("Department code already exists");
        }
        if (departmentRepository.existsByName(dto.getName())) {
            throw new ConflictException("Department name already exists");
        }
        Department entity = mapper.toDepartmentEntity(dto);
        Department saved = departmentRepository.save(entity);
        return mapper.toDepartmentResponse(saved);
    }

    @Override
    @Transactional
    public DepartmentResponseDto update(Long id, DepartmentRequestDto dto) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
        if (!existing.getCode().equals(dto.getCode()) && departmentRepository.existsByCode(dto.getCode())) {
            throw new ConflictException("Department code already exists");
        }
        if (!existing.getName().equals(dto.getName()) && departmentRepository.existsByName(dto.getName())) {
            throw new ConflictException("Department name already exists");
        }
        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setDescription(dto.getDescription());
        Department saved = departmentRepository.save(existing);
        return mapper.toDepartmentResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id " + id);
        }
        if (!employeeRepository.findByDepartment_Id(id).isEmpty()) {
            throw new ConflictException("Cannot delete department with assigned employees");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentResponseDto getById(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
        return mapper.toDepartmentResponse(d);
    }

    @Override
    public Page<DepartmentResponseDto> list(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(mapper::toDepartmentResponse);
    }

    @Override
    public List<EmployeeResponseDto> listEmployees(Long departmentId) {
        return employeeRepository.findByDepartment_Id(departmentId).stream()
                .map(mapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<DepartmentResponseDto> getAll() {
        return departmentRepository.findAll().stream()
                .map(mapper::toDepartmentResponse)
                .collect(Collectors.toList());
    }
}
