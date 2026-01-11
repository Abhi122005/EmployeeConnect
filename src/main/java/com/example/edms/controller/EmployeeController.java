package com.example.edms.controller;

import com.example.edms.dto.EmployeeRequestDto;
import com.example.edms.dto.EmployeeResponseDto;
import com.example.edms.dto.SalaryStatementDto;
import com.example.edms.entity.Employee;
import com.example.edms.mapper.DtoMapper;
import com.example.edms.service.EmployeeService;
import com.example.edms.service.ImageUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ImageUploadService imageUploadService;
    private final DtoMapper mapper;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DtoMapper mapper, ImageUploadService imageUploadService) {
        this.employeeService = employeeService;
        this.mapper = mapper;
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<?> uploadProfileImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }
        try {
            String imageUrl = imageUploadService.uploadImage(file);
            Employee updatedEmployee = employeeService.updateProfileImageUrl(id, imageUrl);
            return ResponseEntity.ok(mapper.toEmployeeResponse(updatedEmployee));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the image.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployeesAsList() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @PostMapping("/delete-batch")
    public ResponseEntity<Void> deleteMultipleEmployees(@RequestBody List<Long> ids) {
        employeeService.deleteMultiple(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDto> getCurrentEmployee(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String employeeId = authentication.getName();
        EmployeeResponseDto dto = employeeService.getByEmployeeId(employeeId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{employeeId}/salary-statement")
    public ResponseEntity<SalaryStatementDto> getSalaryStatement(@PathVariable String employeeId) {
        SalaryStatementDto dto = employeeService.getSalaryStatementByEmployeeId(employeeId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{employeeId}/salary-history")
    public ResponseEntity<List<SalaryStatementDto>> getSalaryHistory(@PathVariable String employeeId) {
        List<SalaryStatementDto> history = employeeService.getSalaryStatementHistory(employeeId);
        return ResponseEntity.ok(history);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto created = employeeService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto updated = employeeService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        EmployeeResponseDto dto = employeeService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> searchEmployees(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeResponseDto> result = employeeService.search(employeeId, name, role, department, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/assign-department")
    public ResponseEntity<EmployeeResponseDto> assignDepartment(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long departmentId = ((Number) body.get("departmentId")).longValue();
        employeeService.assignDepartment(id, departmentId);
        EmployeeResponseDto updated = employeeService.getById(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String employeeId = body.get("employeeId");
            String token = employeeService.generateResetToken(employeeId);
            return ResponseEntity.ok(Map.of("resetToken", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        boolean success = employeeService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }
}