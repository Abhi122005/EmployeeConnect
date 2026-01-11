UX Flow & Sample Screenshots (placeholders)

Flow 1: List employees -> search -> view details
- Start: Open homepage -> #employeesTable displays list via GET /api/employees
- Search: type in #globalSearch -> calls GET /api/employees?name=<q>
- View: click "View" -> modal (#employeeDetail) opens displaying EmployeeResponseDto
Screenshot placeholder: https://example.com/screenshots/employees-list.png

Flow 2: Create employee form -> validation messages
- Open "Employees" -> Fill #employeeForm inputs (employeeId, firstName, lastName, role, salary, joiningDate, experienceYears)
- Submit -> POST /api/employees
- On validation failure the UI shows messages in the global error banner (#globalError)
Screenshot placeholder: https://example.com/screenshots/create-employee.png

Flow 3: Department management -> assign employee
- Open "Departments" -> Create department via #departmentForm (POST /api/departments)
- Assign employee: Use department actions -> "Employees" to view members OR use POST /api/employees/{id}/assign-department
Screenshot placeholder: https://example.com/screenshots/assign-dept.png

DOM IDs updated by the SPA:
- #employeesTable, #departmentsTable, #employeeForm, #departmentForm, #departmentSelect, #globalSearch, #employeeDetail

Replace placeholders with real screenshots when available.
