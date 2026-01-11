package com.example.edms.repository;

import com.example.edms.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Employee entities with search and filter capabilities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);

    long countByCurrentlyWorking(boolean isWorking);

    List<Employee> findByDepartment_Id(Long departmentId);

    /**
     * This method is required to fetch all employees except those with a specific role,
     * which is used to hide admins from the general employee list.
     * @param role The role to exclude (e.g., "System Admin").
     * @return A list of employees that do not have the specified role.
     */
    List<Employee> findAllByRoleNotIgnoreCase(String role);

    @Query("SELECT e FROM Employee e LEFT JOIN e.department d " +
            "WHERE (:employeeId IS NULL OR e.employeeId = :employeeId) " +
            "AND (:name IS NULL OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:role IS NULL OR LOWER(e.role) = LOWER(:role)) " +
            "AND (:department IS NULL OR LOWER(d.code) = LOWER(:department))")
    Page<Employee> search(@Param("employeeId") String employeeId,
                          @Param("name") String name,
                          @Param("role") String role,
                          @Param("department") String department,
                          Pageable pageable);

}
