package com.example.edms.controller;

import com.example.edms.dto.DepartmentRequestDto;
import com.example.edms.dto.DepartmentResponseDto;
import com.example.edms.dto.EmployeeResponseDto;
import com.example.edms.service.DepartmentService;
import com.example.edms.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @Autowired
    public DepartmentController(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartmentsAsList() {
        return ResponseEntity.ok(departmentService.getAll());
    }
    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(@Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto created = departmentService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto updated = departmentService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartment(@PathVariable Long id) {
        DepartmentResponseDto dto = departmentService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDto>> listDepartments(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DepartmentResponseDto> result = departmentService.list(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeResponseDto>> listEmployeesInDepartment(@PathVariable Long id) {
        List<EmployeeResponseDto> list = departmentService.listEmployees(id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/assign-employee")
    public ResponseEntity<EmployeeResponseDto> assignEmployeeToDepartment(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        if (!body.containsKey("employeeId")) {
            return ResponseEntity.badRequest().build();
        }
        Number employeeIdNum;
        try {
            employeeIdNum = (Number) body.get("employeeId");
        } catch (ClassCastException ex) {
            return ResponseEntity.badRequest().build();
        }
        Long employeeId = employeeIdNum.longValue();
        employeeService.assignDepartment(employeeId, id);
        EmployeeResponseDto updated = employeeService.getById(employeeId);
        return ResponseEntity.ok(updated);
    }
}
