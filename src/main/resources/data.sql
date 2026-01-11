INSERT INTO departments (id, dept_name, employee_count, code, description, created_at, updated_at) VALUES (1,'Human Resources', 25, 'HR', 'Handles recruitment and employee relations', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO departments (id, dept_name, employee_count, code, description, created_at, updated_at) VALUES (2,'Engineering', 50, 'ENG', 'Product engineering and development', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert data into the employees table.
-- The department_id values now correctly reference the BIGINT IDs from the departments table.
INSERT INTO employees (employee_id, first_name, last_name, full_name, role, salary, joining_date, relieving_date, currently_working, experience_years, department_id, created_at, updated_at) VALUES ('EMP001','Alice','Smith','Alice Smith','Developer',75000.00,'2018-05-01', NULL, TRUE, 4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO employees (employee_id, first_name, last_name, full_name, role, salary, joining_date, relieving_date, currently_working, experience_years, department_id, created_at, updated_at) VALUES ('EMP002','Bob','Johnson','Bob Johnson','HR Manager',65000.00,'2016-03-15', NULL, TRUE, 7, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
