package com.example.edms.dto;

import java.math.BigDecimal;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class SalaryStatementDto {

    private String employeeId;
    private BigDecimal salary;
    private LocalDate joiningDate;
    private LocalDate relievingDate;
    private Boolean currentlyWorking;
    private String period;
    public SalaryStatementDto() {
    }

    public SalaryStatementDto(String employeeId, BigDecimal salary, LocalDate joiningDate, LocalDate relievingDate, Boolean currentlyWorking) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.relievingDate = relievingDate;
        this.currentlyWorking = currentlyWorking;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public LocalDate getRelievingDate() {
        return relievingDate;
    }

    public Boolean getCurrentlyWorking() {
        return currentlyWorking;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setRelievingDate(LocalDate relievingDate) {
        this.relievingDate = relievingDate;
    }

    public void setCurrentlyWorking(Boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }
}
