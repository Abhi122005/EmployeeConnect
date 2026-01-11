package com.example.edms.service.impl;

import com.example.edms.dto.EmployeeRequestDto;
import com.example.edms.dto.EmployeeResponseDto;
import com.example.edms.dto.SalaryStatementDto;
import com.example.edms.entity.Department;
import com.example.edms.entity.Employee;
import com.example.edms.entity.EmployeePasswordReset;
import com.example.edms.exception.ConflictException;
import com.example.edms.exception.ResourceNotFoundException;
import com.example.edms.mapper.DtoMapper;
import com.example.edms.repository.DepartmentRepository;
import com.example.edms.repository.EmployeePasswordResetRepository;
import com.example.edms.repository.EmployeeRepository;
import com.example.edms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DtoMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeePasswordResetRepository employeeResetRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, DtoMapper mapper, PasswordEncoder passwordEncoder, EmployeePasswordResetRepository employeeResetRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.employeeResetRepository = employeeResetRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryStatementDto> getSalaryStatementHistory(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with employeeId " + employeeId));
        if (employee.getJoiningDate() == null) {
            return Collections.emptyList();
        }
        List<SalaryStatementDto> history = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        YearMonth startMonth = YearMonth.from(employee.getJoiningDate());
        YearMonth endMonth = YearMonth.from(LocalDate.now());
        if (employee.getRelievingDate() != null && employee.getRelievingDate().isBefore(LocalDate.now())) {
            endMonth = YearMonth.from(employee.getRelievingDate());
        }
        for (YearMonth currentMonth = startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth.plusMonths(1)) {
            SalaryStatementDto statement = new SalaryStatementDto();
            statement.setEmployeeId(employee.getEmployeeId());
            statement.setSalary(employee.getSalary());
            statement.setPeriod(currentMonth.format(formatter));
            history.add(statement);
        }
        return history;
    }

    @Override
    @Transactional
    public String generateResetToken(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        String token = UUID.randomUUID().toString();
        EmployeePasswordReset reset = new EmployeePasswordReset();
        reset.setEmployee(employee);
        reset.setToken(token);
        reset.setExpiry(Instant.now().plusSeconds(900)); // 15 min expiry
        employeeResetRepository.save(reset);
        return token;
    }

    @Override
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        EmployeePasswordReset reset = employeeResetRepository.findByToken(token).orElse(null);

        if (reset == null || reset.getExpiry().isBefore(Instant.now())) {
            if (reset != null) employeeResetRepository.delete(reset);
            return false;
        }

        Employee employee = reset.getEmployee();
        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        employeeResetRepository.delete(reset); // Invalidate the token after use
        return true;
    }

    // --- All other existing methods below ---

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAll() {
        return employeeRepository.findAllByRoleNotIgnoreCase("System Admin").stream()
                .map(mapper::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponseDto create(EmployeeRequestDto dto) {
        if (employeeRepository.existsByEmployeeId(dto.getEmployeeId())) {
            throw new ConflictException("employeeId already exists");
        }
        Department department = null;
        if (dto.getDepartmentId() != null) {
            department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + dto.getDepartmentId()));
        }
        Employee entity = mapper.toEmployeeEntity(dto, department);
        Employee saved = employeeRepository.save(entity);
        return mapper.toEmployeeResponse(saved);
    }

    @Override
    @Transactional
    public EmployeeResponseDto update(Long id, EmployeeRequestDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        if (!existing.getEmployeeId().equals(dto.getEmployeeId()) && employeeRepository.existsByEmployeeId(dto.getEmployeeId())) {
            throw new ConflictException("employeeId already exists");
        }
        existing.setEmployeeId(dto.getEmployeeId());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setRole(dto.getRole());
        existing.setSalary(dto.getSalary());
        existing.setJoiningDate(dto.getJoiningDate());
        existing.setRelievingDate(dto.getRelievingDate());
        existing.setCurrentlyWorking(dto.getCurrentlyWorking());
        existing.setExperienceYears(dto.getExperienceYears());
        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + dto.getDepartmentId()));
            existing.setDepartment(dept);
        } else {
            existing.setDepartment(null);
        }
        Employee saved = employeeRepository.save(existing);
        return mapper.toEmployeeResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        employeeRepository.delete(existing);
    }
    @Override
    @Transactional
    public Employee updateProfileImageUrl(Long id, String imageUrl) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setProfileImageUrl(imageUrl);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteMultiple(List<Long> ids) {
        List<Employee> employeesToDelete = employeeRepository.findAllById(ids);
        for (Employee employee : employeesToDelete) {
            employee.setCurrentlyWorking(false);
            employee.setRelievingDate(LocalDate.now());
        }
        employeeRepository.saveAll(employeesToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getById(Long id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        return mapper.toEmployeeResponse(e);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getByEmployeeId(String employeeId) {
        Employee e = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with employeeId " + employeeId));
        return mapper.toEmployeeResponse(e);
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryStatementDto getSalaryStatementByEmployeeId(String employeeId) {
        Employee e = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with employeeId " + employeeId));
        return mapper.toSalaryStatement(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> search(String employeeId, String name, String role, String departmentCode, Pageable pageable) {
        Page<Employee> page = employeeRepository.search(
                employeeId != null && !employeeId.isBlank() ? employeeId : null,
                name != null && !name.isBlank() ? name : null,
                role != null && !role.isBlank() ? role : null,
                departmentCode != null && !departmentCode.isBlank() ? departmentCode : null,
                pageable
        );
        return page.map(mapper::toEmployeeResponse);
    }

    @Override
    @Transactional
    public void assignDepartment(Long employeeId, Long departmentId) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + employeeId));
        Department d = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));
        e.setDepartment(d);
        employeeRepository.save(e);
    }
}