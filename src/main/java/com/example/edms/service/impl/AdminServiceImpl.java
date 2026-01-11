package com.example.edms.service.impl;

import com.example.edms.dto.AdminUpdateRequestDto;
import com.example.edms.dto.SalaryStatementDto;
import com.example.edms.entity.Admin;
import com.example.edms.entity.Department;
import com.example.edms.entity.PasswordReset;
import com.example.edms.exception.ResourceNotFoundException;
import com.example.edms.repository.AdminRepository;
import com.example.edms.repository.DepartmentRepository;
import com.example.edms.repository.PasswordResetRepository;
import com.example.edms.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetRepository resetRepository;
    private final DepartmentRepository departmentRepository;


    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder, PasswordResetRepository resetRepository, DepartmentRepository departmentRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.resetRepository = resetRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Admin> login(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isPresent() && passwordEncoder.matches(password, adminOpt.get().getPassword())) {
            adminOpt.ifPresent(admin -> {
                if (admin.getDepartment() != null) {
                    admin.getDepartment().getName();
                }
            });
            return adminOpt;
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    @Transactional
    public Admin createAdmin(Admin newAdmin) {
        if (adminRepository.findByUsername(newAdmin.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        return adminRepository.save(newAdmin);
    }

    @Override
    @Transactional
    public Admin updateAdmin(Long id, AdminUpdateRequestDto dto) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        if (dto.getFullName() != null) existingAdmin.setFullName(dto.getFullName());
        if (dto.getUsername() != null) existingAdmin.setUsername(dto.getUsername());
        if (dto.getEmail() != null) existingAdmin.setEmail(dto.getEmail());
        if (dto.getRole() != null) existingAdmin.setRole(dto.getRole());
        if (dto.getSalary() != null) existingAdmin.setSalary(dto.getSalary());
        if (dto.getJoiningDate() != null) existingAdmin.setJoiningDate(dto.getJoiningDate());
        if (dto.getExperienceYears() != null) existingAdmin.setExperienceYears(dto.getExperienceYears());

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));
            existingAdmin.setDepartment(department);
        }

        return adminRepository.save(existingAdmin);
    }

    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new IllegalStateException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Admin> findByUsername(String username) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        adminOpt.ifPresent(admin -> {
            if (admin.getDepartment() != null) {
                admin.getDepartment().getName();
            }
        });
        return adminOpt;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryStatementDto> getSalaryStatementHistory(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + username));

        if (admin.getJoiningDate() == null) {
            return Collections.emptyList();
        }

        List<SalaryStatementDto> history = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        YearMonth startMonth = YearMonth.from(admin.getJoiningDate());
        YearMonth endMonth = YearMonth.from(LocalDate.now());

        if (admin.getRelievingDate() != null && admin.getRelievingDate().isBefore(LocalDate.now())) {
            endMonth = YearMonth.from(admin.getRelievingDate());
        }

        for (YearMonth currentMonth = startMonth; !currentMonth.isAfter(endMonth); currentMonth = currentMonth.plusMonths(1)) {
            SalaryStatementDto statement = new SalaryStatementDto();
            statement.setEmployeeId(admin.getUsername());
            statement.setSalary(admin.getSalary());
            statement.setPeriod(currentMonth.format(formatter));
            history.add(statement);
        }

        return history;
    }

    /**
     * This is the new method implementation. It finds an admin by ID,
     * updates their profile image URL, and saves the change to the database.
     */
    @Override
    @Transactional
    public Admin updateProfileImageUrl(Long id, String imageUrl) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
        admin.setProfileImageUrl(imageUrl);
        return adminRepository.save(admin);
    }

    @Override
    @Transactional
    public String generateResetToken(String email) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found with email: " + email));

        String token = UUID.randomUUID().toString();
        PasswordReset reset = new PasswordReset();
        reset.setAdmin(admin);
        reset.setToken(token);
        reset.setExpiry(Instant.now().plusSeconds(900));
        resetRepository.save(reset);
        return token;
    }

    @Override
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordReset> resetOpt = resetRepository.findByToken(token);
        if (resetOpt.isEmpty()) {
            return false;
        }

        PasswordReset reset = resetOpt.get();
        if (reset.getExpiry().isBefore(Instant.now())) {
            resetRepository.delete(reset);
            return false;
        }

        Admin admin = reset.getAdmin();
        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);

        resetRepository.delete(reset);
        return true;
    }
}