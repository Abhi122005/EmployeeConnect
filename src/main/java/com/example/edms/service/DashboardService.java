package com.example.edms.service;

import com.example.edms.repository.DepartmentRepository;
import com.example.edms.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DashboardService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * Gathers statistics for the admin dashboard.
     * @return A map containing counts for total, active, and resigned employees, and total departments.
     */
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();

        // Get total count of all employees
        long totalEmployees = employeeRepository.count();

        // Get count of employees who are currently working (active)
        long activeEmployees = employeeRepository.countByCurrentlyWorking(true);

        // Get count of employees who are not currently working (resigned)
        long resignedEmployees = employeeRepository.countByCurrentlyWorking(false);

        // Get total count of all departments
        long departments = departmentRepository.count();

        // Populate the map with the calculated statistics
        stats.put("totalEmployees", totalEmployees);
        stats.put("activeEmployees", activeEmployees);
        stats.put("resignedEmployees", resignedEmployees);
        stats.put("departments", departments);

        return stats;
    }
}