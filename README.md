
# 🛒 **Store Management Backend API**

This is the backend API for managing a store. It provides endpoints for product management, including CRUD operations, price updates, and search functionality. It is built with **Spring Boot** and uses **JWT** for authentication.

## 🚀 **Features**

- Authentication with **JWT**
- **CRUD** operations for products
- **Price update** functionality
- **Custom search** for products
- Interactive **Swagger UI** for exploring the API
- **Exception handling** for product not found and other errors

## 🛠️ **Technologies Used**

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **H2 Database** (for development/testing)
- **Swagger UI** (via springdoc-openapi)

## 🔧 **Getting Started**

### Clone the Repository

```bash
git clone https://github.com/your-repo/store-management.git
cd store-management
```

### Build and Run the Application

1. **Build the project**:

   To install all necessary dependencies and build the project, run:

   ```bash
   mvn clean install
   ```

2. **Run the application**:

   After building the project, you can run the Spring Boot application with:

   ```bash
   mvn spring-boot:run
   ```

   The app will run on `http://localhost:8080`.

### Swagger UI

The interactive API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

This provides an easy-to-use interface to explore all available endpoints and test them directly.

### Authentication (JWT Token)

To access protected endpoints, you must first **log in** to receive a JWT token. Use this token as a **Bearer token** in the header for subsequent requests.

**Example of Bearer token usage**:

```bash
Authorization: Bearer <your-jwt-token>
```

---

## 📚 **API Endpoints**

### 🛠️ **Product Management**

- **GET /api/products**  
  Fetches all products.

- **GET /api/products/{id}**  
  Retrieves a product by its ID.

- **POST /api/products**  
  Adds a new product.

- **PUT /api/products/{id}**  
  Updates an existing product by ID.

- **DELETE /api/products/{id}**  
  Deletes a product by ID.

### 💸 **Price Update**

- **PATCH /api/products/{id}/price**  
  Updates the price of a product by ID.

### 🔍 **Product Search**

- **GET /api/products/search**  
  Searches for products based on name and minimum price.

### 🚫 **Error Handling**

- **ProductNotFoundException**: Returned with HTTP 404 when a product is not found.
- **Generic Error**: Returns HTTP 500 for unexpected errors.

---


