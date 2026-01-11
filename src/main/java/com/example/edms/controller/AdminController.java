package com.example.edms.controller;

import com.example.edms.dto.AdminResponseDto;
import com.example.edms.dto.AdminUpdateRequestDto;
import com.example.edms.dto.SalaryStatementDto;
import com.example.edms.entity.Admin;
import com.example.edms.mapper.DtoMapper;
import com.example.edms.service.AdminService;
import com.example.edms.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final DtoMapper mapper;
    private final ImageUploadService imageUploadService;

    @Autowired
    public AdminController(AdminService adminService, DtoMapper mapper, ImageUploadService imageUploadService) {
        this.adminService = adminService;
        this.mapper = mapper;
        this.imageUploadService = imageUploadService;
    }

    /**
     * This endpoint handles the profile image upload. It receives the file,
     * passes it to the ImageUploadService, and saves the resulting URL.
     */
    @PostMapping("/{id}/profile-image")
    public ResponseEntity<?> uploadProfileImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }
        try {
            String imageUrl = imageUploadService.uploadImage(file);
            Admin updatedAdmin = adminService.updateProfileImageUrl(id, imageUrl);
            return ResponseEntity.ok(mapper.toAdminResponse(updatedAdmin));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the image.");
        }
    }

    // --- All other existing methods remain below ---

    @GetMapping("/me")
    public ResponseEntity<AdminResponseDto> getCurrentAdmin(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        Optional<Admin> adminOpt = adminService.findByUsername(username);
        return adminOpt.map(admin -> ResponseEntity.ok(mapper.toAdminResponse(admin)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{username}/salary-history")
    public ResponseEntity<List<SalaryStatementDto>> getSalaryHistory(@PathVariable String username) {
        List<SalaryStatementDto> history = adminService.getSalaryStatementHistory(username);
        return ResponseEntity.ok(history);
    }

    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> getAllAdmins() {
        List<Admin> admins = adminService.findAllAdmins();
        List<AdminResponseDto> dtos = admins.stream()
                .map(mapper::toAdminResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<AdminResponseDto> createAdmin(@RequestBody Admin newAdmin) {
        try {
            Admin createdAdmin = adminService.createAdmin(newAdmin);
            return new ResponseEntity<>(mapper.toAdminResponse(createdAdmin), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDto> updateAdmin(@PathVariable Long id, @RequestBody AdminUpdateRequestDto dto) {
        Admin updatedAdmin = adminService.updateAdmin(id, dto);
        return ResponseEntity.ok(mapper.toAdminResponse(updatedAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            String token = adminService.generateResetToken(email);
            return ResponseEntity.ok(Map.of("resetToken", token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        boolean success = adminService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }
}