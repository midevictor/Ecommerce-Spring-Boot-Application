```markdown
# Ecommerce-Spring-Boot-Application
# Ashluxe E-commerce Category Management

This project is a Spring Boot application for managing product categories and products in an e-commerce system. It demonstrates a layered architecture using Controllers, Services, and Repositories, with validation and exception handling.

## Features

\- CRUD operations for product categories and products
\- Validation using `@Valid` and Jakarta Bean Validation
\- Exception handling for common errors
\- In-memory H2 database for development and testing
\- RESTful API endpoints
\- DTO mapping for entities
\- I will be updating the readme with additional features as the project progresses.

## Technologies Used

\- Java
\- Spring Boot
\- Spring Data JPA
\- H2 Database
\- Maven
\- Jakarta Validation
\- ModelMapper for DTO mapping

## Project Structure

\- `model/` — Entity classes (e.g., `Category`, `Product`)
\- `dto/` — Data Transfer Objects (e.g., `CategoryDTO`, `ProductDTO`)
\- `controller/` — REST controllers (e.g., `CategoryController`, `ProductController`)
\- `service/` — Business logic (e.g., `CategoryService`, `ProductService`, `ProductServiceImpl`)
\- `repositories/` — Data access layer (e.g., `CategoryRepository`, `ProductRepository`)
\- `exceptions/` — Custom exception classes

## Endpoints

| Method | Endpoint                        | Description                | Access   |
|--------|---------------------------------|----------------------------|----------|
| GET    | `/api/public/categories`        | List all categories        | Public   |
| POST   | `/api/admin/categories`         | Add a new category         | Admin    |
| DELETE | `/api/admin/categories/{id}`    | Delete a category by ID    | Admin    |
| PUT    | `/api/admin/categories/{id}`    | Update a category by ID    | Admin    |
| GET    | `/api/public/products`          | List all products          | Public   |
| POST   | `/api/admin/products`           | Add a new product          | Admin    |
| DELETE | `/api/admin/products/{id}`      | Delete a product by ID     | Admin    |
| PUT    | `/api/admin/products/{id}`      | Update a product by ID     | Admin    |

## Validation

\- Uses `@Valid` on request bodies to enforce constraints (e.g., not null, size).
\- Validation errors return appropriate HTTP error responses.

## Exception Handling

Handles:
\- Resource not found
\- Duplicate category or product names
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

## Example Entity Classes

### Category Entity
```java
public class Category {
    private Long id;
    private String categoryName;
    private Integer version;
    // getters and setters
}
```

### Product Entity
```java
public class Product {
    private Long productId;
    private String productName;
    private String description;
    private Integer quantity;
    private double price;
    private double specialPrice;
    private String image;
    private double discount;
    private Category category;
    // getters and setters
}
```
```