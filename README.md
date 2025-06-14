# Ecommerce-Spring-Boot-Application
# Ashluxe E-commerce Category Management

This project is a Spring Boot application for managing product categories in an e-commerce system. It demonstrates a layered architecture using Controllers, Services, and Repositories, with validation and exception handling.

## Features

\- CRUD operations for product categories  
\- Validation using `@Valid` and Jakarta Bean Validation  
\- Exception handling for common errors  
\- In-memory H2 database for development and testing  
\- RESTful API endpoints
\- I will be updating the readme with additional features as the project progresses.

## Technologies Used

\- Java  
\- Spring Boot  
\- Spring Data JPA  
\- H2 Database  
\- Maven  
\- Jakarta Validation

## Project Structure

\- `model/` — Entity classes (e.g., `Category`)  
\- `controller/` — REST controllers (e.g., `CategoryController`)  
\- `service/` — Business logic (e.g., `CategoryService`, `CategoryServiceImpl`)  
\- `repositories/` — Data access layer (e.g., `CategoryRepository`)  
\- `exceptions/` — Custom exception classes

## Endpoints

| Method | Endpoint                        | Description                | Access   |
|--------|---------------------------------|----------------------------|----------|
| GET    | `/api/public/categories`        | List all categories        | Public   |
| POST   | `/api/admin/categories`         | Add a new category         | Admin    |
| DELETE | `/api/admin/categories/{id}`    | Delete a category by ID    | Admin    |
| PUT    | `/api/admin/categories/{id}`    | Update a category by ID    | Admin    |

## Validation

\- Uses `@Valid` on request bodies to enforce constraints (e.g., not null, size).  
\- Validation errors return appropriate HTTP error responses.

## Exception Handling

Handles:  
\- Resource not found  
\- Duplicate category names  
\- Validation errors

## Database

\- Uses H2 in-memory database for development.  
\- Connection details in `src/main/resources/application.properties`.

## How to Run

1. Clone the repository.
2. Build with Maven:  
   `mvn clean install`
3. Run the application:  
   `mvn spring-boot:run`
4. Access the H2 console at `/h2-console` (if enabled).

## Example Category Entity

```java
public class Category {
    private Long id;
    private String categoryName;
    private Integer version;
    // getters and setters
}
