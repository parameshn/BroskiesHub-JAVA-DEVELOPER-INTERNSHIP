# Task Management Application (Spring Boot + JUnit + Mockito)

A simple Spring Boot REST API for managing tasks with full unit tests, mocking, and test coverage setup.

## Features
- CRUD operations for Task entity (GET, POST, PUT, DELETE)
- Spring Data JPA repository with custom query methods
- Unit tests using:
  - JUnit 5
  - Mockito
  - MockMvc
  - AssertJ
- Integration tests with in-memory H2 database
- JaCoCo test coverage reports

## Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA (H2 for testing)
- JUnit 5
- Mockito
- AssertJ
- JaCoCo (for test coverage)

## Project Structure
```
src/main/java/com/example
 ├── controller      # REST controllers
 ├── entity          # JPA entities
 ├── repository      # JPA repositories
 ├── service         # Business logic
 └── TaskManagementApplication.java  # Main application entrypoint

src/test/java/com/example
 ├── controller      # Controller unit & integration tests
 ├── service         # Service layer tests
 ├── repository      # Repository tests
 └── TaskTestSuite   # Test suite runner
```

## How to Run
### 1. Clone the project
```
git clone https://github.com/your-username/task-management.git
cd task-management
```

### 2. Build with Maven
```
mvn clean install
```

### 3. Run the application
```
mvn spring-boot:run
```
The app will start at:
http://localhost:8080/api/tasks

## Running Tests
Run all unit & integration tests:
```
mvn test
```

## Example API Requests
### Get all tasks
```
GET /api/tasks
```

### Get task by ID
```
GET /api/tasks/1
```

### Create task
```
POST /api/tasks
Content-Type: application/json

{
  "title": "Learn Spring Boot",
  "description": "Study the documentation"
}
```

### Update task
```
PUT /api/tasks/1
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated description",
  "completed": true
}
```

### Delete task
```
DELETE /api/tasks/1
```

### Get tasks by status
```
GET /api/tasks/status?completed=true
```

## Test Coverage
Generate JaCoCo coverage report:
```
mvn test jacoco:report
```
Open the report in your browser:
```
target/site/jacoco/index.html
```

## Notes
- Default test database: H2 (in-memory)
- Profiles:
  - dev → for local development
  - test → for running unit & integration tests

## Author
Developed by Paramesh N – Backend Developer passionate about Spring Boot, clean architecture, and test-driven development.
