# SAVR Budgeting API

A secure and user-centric REST API built with Spring Boot for managing personal budgets, expenses, and category-wise spending limits. It supports JWT-based authentication, role-based access control, and a PostgreSQL backend.

---

## Features

* User registration and login via JWT authentication
* Role-based access control (admin and user roles)
* Create, retrieve, update, and delete budgets
* Assign spending limits to specific categories
* Log and filter expenses
* CSV expense upload functionality
* OpenAPI (Swagger) documentation for all endpoints

---

## Tech Stack

* **Java 17** + **Spring Boot 3**
* **PostgreSQL**
* **Spring Security + JWT**
* **Lombok**
* **OpenAPI / Swagger UI**
* **JUnit + Mockito** for testing

---

## API Documentation

Interactive Swagger UI available at:

```
http://localhost:8080/swagger-ui/index.html
```

It includes detailed descriptions, request/response examples, and parameter annotations.

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/budget-api.git
cd budget-api
```

### 2. Configure Environment Variables

Create a `.env` file or set environment variables:

```
DB_URL=jdbc:postgresql://localhost:5432/budget_db
DB_USERNAME=yourusername
DB_PASSWORD=yourpassword
JWT_SECRET=your_jwt_secret
```

Or configure directly in `application.yml` or `application.properties`.

### 3. Run PostgreSQL

Ensure PostgreSQL is running and a database named `budget_db` is created.

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

---

## API Usage Examples

### Register a User

```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePass123"
}
```

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePass123"
}
```

Response:

```json
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

Use this token in headers:

```
Authorization: Bearer <your-token>
```

### Create Budget

```http
POST /budget
Authorization: Bearer <token>
Content-Type: application/json

{
  "totalIncome": 5000,
  "totalLimit": 1500
}
```

### Upload CSV Expenses

```http
POST /expenses/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: expenses.csv
```

> CSV format must be: `date, amount, title`

---

## Running Tests

```bash
./mvnw test
```

Includes unit and integration tests for service and controller layers.

---

## Deployment

You can deploy using Docker, Heroku, or any Spring-compatible cloud service.

### Sample Dockerfile

```Dockerfile
FROM openjdk:21
COPY target/budget-api.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
./mvnw package
docker build -t budget-api .
docker run -p 8080:8080 budget-api
```

---

## Contribution

Pull requests will be welcome, once CI/CD and unit testing has been integrated.

---

## Author

**Loban Matin**
[LinkedIn](https://www.linkedin.com/in/loban-matin/) | [GitHub](https://github.com/LobanMatin)

---
