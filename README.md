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
* **PostgreSQL (13)**
* **Spring Security + JWT**
* **Lombok**
* **OpenAPI / Swagger UI**
* **JUnit + Mockito** for testing

---

## Deployment

Both the database and API have been deployed on render, with the following end point for the API:

```
https://savr-api-r6be.onrender.com
```

PRs to main need to be reviewed and pass the CI/CD pipeline through github actions to be succefully deployed on render.

---

## API Documentation

Interactive Swagger UI available at:

```
https://savr-api-r6be.onrender.com/swagger-ui/index.html
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
POSTGRES_DB=db_name
POSTGRES_USER=db_user
POSTGRES_PASSWORD=db_pass
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/db_name
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_pass
JWT_SECRET=your_secret
JWT_EXPIRATION=8640000
```

Or configure directly in `application.yml` or `application.properties`.

### 3. Run API locally

```bash
docker compose up --build
```

---
## Author

**Loban Matin**
[LinkedIn](https://www.linkedin.com/in/loban-matin/) | [GitHub](https://github.com/LobanMatin)

---
