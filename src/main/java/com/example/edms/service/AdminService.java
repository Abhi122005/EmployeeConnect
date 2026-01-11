package com.example.edms.service;
import com.example.edms.dto.AdminResponseDto;
import com.example.edms.dto.AdminUpdateRequestDto;
import com.example.edms.dto.SalaryStatementDto;
import com.example.edms.entity.Admin;
import java.util.Optional;
import java.util.List;

public interface AdminService {
    Optional<Admin> login(String username, String password);
    List<Admin> findAllAdmins();
    Admin createAdmin(Admin newAdmin);
    Admin updateAdmin(Long id, AdminUpdateRequestDto dto);
    void deleteAdmin(Long id);
    Optional<Admin> findByUsername(String username);
    String generateResetToken(String email);
    boolean resetPassword(String token, String newPassword);
    List<SalaryStatementDto> getSalaryStatementHistory(String username);
    Admin updateProfileImageUrl(Long id, String imageUrl);
}

