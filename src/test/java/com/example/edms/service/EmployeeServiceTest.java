package com.example.edms.service;

import com.example.edms.dto.EmployeeRequestDto;
import com.example.edms.dto.EmployeeResponseDto;
import com.example.edms.dto.SalaryStatementDto;
import com.example.edms.entity.Department;
import com.example.edms.entity.Employee;
import com.example.edms.exception.ConflictException;
import com.example.edms.mapper.DtoMapper;
import com.example.edms.repository.DepartmentRepository;
import com.example.edms.repository.EmployeePasswordResetRepository;
import com.example.edms.repository.EmployeeRepository;
import com.example.edms.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    private DtoMapper mapper;
    private PasswordEncoder passwordEncoder;
    private EmployeePasswordResetRepository employeePasswordResetRepository;
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setup() {
        // Mock all required dependencies
        employeeRepository = mock(EmployeeRepository.class);
        departmentRepository = mock(DepartmentRepository.class);
        mapper = new DtoMapper();
        passwordEncoder = mock(PasswordEncoder.class);
        employeePasswordResetRepository = mock(EmployeePasswordResetRepository.class);

        // Correctly initialize the service with all five dependencies
        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                departmentRepository,
                mapper,
                passwordEncoder,
                employeePasswordResetRepository
        );
    }

    @Test
    void testCreateEmployeeSuccess() {
        EmployeeRequestDto dto = new EmployeeRequestDto();
        dto.setEmployeeId("EMP2001");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setRole("Dev");
        dto.setSalary(BigDecimal.valueOf(50000));
        dto.setJoiningDate(LocalDate.of(2020, 1, 5));
        dto.setCurrentlyWorking(true);
        dto.setExperienceYears(3);
        dto.setDepartmentId(2L);

        when(employeeRepository.existsByEmployeeId("EMP2001")).thenReturn(false);

        Department dept = new Department();
        dept.setName("Engineering");
        dept.setCode("ENG");

        when(departmentRepository.findById(2L)).thenReturn(Optional.of(dept));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponseDto res = employeeService.create(dto);
        assertNotNull(res);
        assertEquals("EMP2001", res.getEmployeeId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployeeDuplicateEmployeeId() {
        EmployeeRequestDto dto = new EmployeeRequestDto();
        dto.setEmployeeId("EMP2001");
        when(employeeRepository.existsByEmployeeId("EMP2001")).thenReturn(true);
        assertThrows(ConflictException.class, () -> employeeService.create(dto));
    }

    @Test
    void testGetSalaryStatement() {
        Employee e = new Employee();
        e.setEmployeeId("EMP1001");
        e.setSalary(BigDecimal.valueOf(75000));
        e.setJoiningDate(LocalDate.of(2018, 5, 1));
        e.setRelievingDate(null);
        e.setCurrentlyWorking(true);

        when(employeeRepository.findByEmployeeId("EMP1001")).thenReturn(Optional.of(e));

        SalaryStatementDto s = employeeService.getSalaryStatementByEmployeeId("EMP1001");
        assertNotNull(s);
        assertEquals("EMP1001", s.getEmployeeId());
        assertEquals(BigDecimal.valueOf(75000), s.getSalary());
    }
}


