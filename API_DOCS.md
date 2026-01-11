# API Documentation

All endpoints are prefixed with /api

Employees
- GET /api/employees
  - Query params:
    - employeeId (exact)
    - name (partial match on fullName)
    - role (exact)
    - department (department code exact)
    - page (int, default=0)
    - size (int, default=20)
    - sort (e.g., "lastName,asc")
  - Response: Page<EmployeeResponseDto>
  - Example:
    {
      "content": [
        {
          "id": 1,
          "employeeId": "EMP1001",
          "firstName": "Alice",
          "lastName": "Smith",
          "fullName": "Alice Smith",
          "role": "Developer",
          "salary": 75000.00,
          "joiningDate": "2018-05-01",
          "relievingDate": null,
          "currentlyWorking": true,
          "experienceYears": 4,
          "department": { "id": 2, "name": "Engineering", "code": "ENG", ... },
          "createdAt": "...",
          "updatedAt": "..."
        }
      ],
      "pageable": { ... },
      "totalElements": 2,
      "totalPages": 1,
      "number": 0,
      "size": 20
    }

- POST /api/employees
  - Request body: EmployeeRequestDto
    {
      "employeeId":"EMP2001",
      "firstName":"John",
      "lastName":"Doe",
      "role":"Dev",
      "salary":50000,
      "joiningDate":"2020-01-05",
      "relievingDate": null,
      "currentlyWorking": true,
      "experienceYears": 3,
      "departmentId": 2
    }
  - Response: 201 Created, body EmployeeResponseDto

- PUT /api/employees/{id}
  - Update employee by numeric id, request body same as create.
  - Response: 200 OK, EmployeeResponseDto

- DELETE /api/employees/{id}
  - Response: 204 No Content

- GET /api/employees/{id}
  - Get employee by numeric id

- GET /api/employees/by-employeeId/{employeeId}
  - Get employee by business employeeId

- GET /api/employees/{employeeId}/salary-statement
  - Returns SalaryStatementDto:
    {
      "employeeId":"EMP1001",
      "salary":75000.00,
      "joiningDate":"2018-05-01",
      "relievingDate": null,
      "currentlyWorking": true
    }

- POST /api/employees/{id}/assign-department
  - Body: { "departmentId": <long> }
  - Response: 200 EmployeeResponseDto (updated)

Departments
- GET /api/departments?page=0&size=20
  - Returns Page<DepartmentResponseDto>

- POST /api/departments
  - Body: { "name": "...", "code": "...", "description": "..." }
  - Response: 201 Created, DepartmentResponseDto

- PUT /api/departments/{id}
  - Update department

- DELETE /api/departments/{id}
  - 204 No Content (409 if employees are assigned)

- GET /api/departments/{id}/employees
  - Returns list of EmployeeResponseDto assigned to the department

- POST /api/departments/{id}/assign-employee
  - Body: { "employeeId": <numeric employee id> }
  - Response: 200 EmployeeResponseDto

Error responses
- 400 Validation:
  {
    "error": "Validation Failed",
    "errors": [
      { "field": "employeeId", "rejectedValue": null, "message": "must not be blank" }
    ]
  }

- 404 Not Found:
  { "error": "Not Found", "message": "Employee not found with id 123" }

- 409 Conflict:
  { "error": "Conflict", "message": "employeeId already exists" }

- 500 Internal:
  { "error": "Internal Server Error", "message": "..." }
