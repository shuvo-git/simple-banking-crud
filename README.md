# Simple Banking CRUD

A Java Spring Boot application for managing bank accounts and transactions, featuring comprehensive CRUD operations, search filtering, and transaction history.

## Technology Stack
- **Java**: 17
- **Framework**: Spring Boot 3.3.0
- **Database**: H2 (In-Memory)
- **Build Tool**: Maven
- **Documentation**: SpringDoc OpenAPI (Swagger)

## Getting Started

### Prerequisites
- JDK 17
- Maven

### Running the Application
```bash
./mvnw spring-boot:run
```

The application will start on port `8080`.

## Documentation & Tools

### Swagger UI
Interact with the API endpoints via the Swagger UI:
- **URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### H2 Console
Access the in-memory database to query data directly:
- **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL**: `jdbc:h2:mem:bankingdb`
- **Username**: `sa`
- **Password**: `password`

## Initial Data
Upon startup, the application seeds the database with the following accounts for demonstration purposes:

| Account Number | Owner | Initial Balance |
| :--- | :--- | :--- |
| `102023001` | John Doe | 5000.00 |
| `102023002` | Jane Smith | 12500.50 |

You can use these account numbers to test endpoints like `GET /api/accounts/{accountNumber}`.
