# Employee Data Management System (EDMS)

A minimal Employee Data Management System built with Spring Boot (Java) backend and a vanilla HTML/CSS/JS single-page frontend. The app uses H2 in-memory database by default and ships sample data to try features quickly.

Features:
- Employee CRUD: create, read, update, delete
- Department CRUD: create, read, update, delete
- Search employees by employeeId, name (partial), role, department code
- Assign/reassign employees to departments
- Salary statement retrieval for an employee
- Simple responsive frontend (static files served by Spring Boot)

Prerequisites
- Java 17+
- Maven 3.6+
- Docker (optional, to build/run container)

Build & Run (H2 in-memory, default)
1. Build:
   mvn clean package

2. Run:
   java -jar target/employee-data-management-system-0.0.1-SNAPSHOT.jar

3. H2 Console:
   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:edmsdb
   User: sa
   Password: (empty)

Switch to PostgreSQL
1. Create database and user in your Postgres instance.
2. Provide database info using environment variables or edit `src/main/resources/application.properties`.
   Example using env:
   export SPRING_PROFILES_ACTIVE=postgres
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/edmsdb
   export SPRING_DATASOURCE_USERNAME=postgres
   export SPRING_DATASOURCE_PASSWORD=postgres

3. Run application:
   java -jar target/employee-data-management-system-0.0.1-SNAPSHOT.jar

Docker
1. Build image:
   docker build -t edms .

2. Run container:
   docker run -p 8080:8080 edms

   To use Postgres profile with docker:
   docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=postgres -e SPRING_DATASOURCE_URL=... -e SPRING_DATASOURCE_USERNAME=... -e SPRING_DATASOURCE_PASSWORD=... edms

API Examples (curl)
- Create employee:
  curl -X POST -H "Content-Type: application/json" -d '{"employeeId":"EMP2001","firstName":"John","lastName":"Doe","role":"Dev","salary":50000,"joiningDate":"2020-01-05","currentlyWorking":true,"experienceYears":3,"departmentId":2}' http://localhost:8080/api/employees

- Search:
  curl "http://localhost:8080/api/employees?name=Alice&role=Developer&department=ENG&page=0&size=20&sort=lastName,asc"

- Salary statement:
  curl http://localhost:8080/api/employees/EMP1001/salary-statement

Testing
- Run unit & integration tests:
  mvn test

Project Structure
- src/main/java/com/example/edms: Java source code (controllers, services, repositories, entities, DTOs)
- src/main/resources/static: frontend assets (index.html, styles.css, app.js)
- src/main/resources/data.sql: seed data inserted into H2 dev DB on startup

Notes & Extensibility
- DTOs are used for all API requests/responses; entities are not exposed directly.
- Add authentication: include `spring-boot-starter-security` and configure SecurityFilterChain.
- Add JWT: include JJWT and implement authentication filters.
- Lombok is included but not required; if you enable Lombok in your IDE, you may refactor entities/DTOs.

License
This project is licensed under the MIT License - see LICENSE.txt
