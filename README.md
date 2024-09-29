
# <p align=center>Simple Finance API</p>

The **Simple Finance API** is a Java application developed using Spring Boot, designed to manage financial transactions simply and efficiently. This API allows user registration, JWT authentication, and the management of banking transactions such as deposits, withdrawals, and transfers.

## Features

- **Authentication and Authorization:**
  - JWT (JSON Web Tokens) based authentication.
  - Authentication with HttpOnly cookies for session management.
  
- **Transaction Management:**
  - Create, read, update, and delete transactions (CRUD).
  - Support for different transaction types like deposit, withdrawal, and transfer.
  - Filtering by `id` and `type` when querying transactions.

  
## Technologies Used

- **Java 22** 
- **Spring Boot** - Framework for building the API.
  - **Spring Security** - For authentication and authorization using JWT.
  - **Spring Data JPA** - For object-relational mapping.
  - **Spring Web** - For creating REST endpoints.
  
- **PostgreSQL** - Relational database used.
- **Flyway** - Database version control.
- **JUnit & Mockito** - Unit testing.
- **Swagger (OpenAPI 3)** - Automatic API documentation.

## Installation

### Prerequisites

- Java 22
- Maven 3+
- PostgreSQL

### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/rleo05/simple-finance-api.git
   cd simple-finance-api
   ```

2. Set up the PostgreSQL database:
   - Create a PostgreSQL database.
   - Update the `application.properties` file with the correct database configurations:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
     spring.datasource.username=${DB_USERNAME}
     spring.datasource.password=${DB_PASSWORD}
     ```

3. Update the `application.properties` file with the JWT configuration:
   ```properties
   jwt.token.secret.key=${JWT_SECRET_KEY}
   ```     

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

5. Access the API documentation on Swagger:
   - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Environment Variables

The application requires the following environment variables to be set:

- `DB_USERNAME`: The username for your PostgreSQL database.
- `DB_PASSWORD`: The password for your PostgreSQL database.
- `JWT_SECRET_KEY`: The secret key used for signing JWT tokens.

You can set these variables in your environment or configure them in a `.env` file or directly in your application's configuration file, depending on your setup.

## Usage

### Authentication

Before accessing transaction endpoints, you need to authenticate. Log in to obtain a JWT Token.

- **Login Endpoint:**
  - `POST /auth/login`
  - Body:
    ```json
    {
      "username": "user",
      "password": "password"
    }
    ```

- The JWT token will be returned in the response header as a HttpOnly cookie. It will be automatically included in subsequent requests.

### Example Transaction Request

- **Endpoint:**
  - `POST /transaction/{document}/transfer`
  
- **Request Body (Example):**
  ```json
  {
    "receiver_document": "111111111",
    "amount": 500
  }
  ```

- **Response Body (Example):**
  ```json
  {
    "sender_name": "name1",
    "sender_document": "999999999",
    "receiver_name": "name2",
    "receiver_document": "111111111",
    "amount": 500,
    "timestamp": "2024-09-29T02:57:06.606282900Z"
  }
  ```

- **Authorization:**
  - After a successful authentication, you will be automatically authorized using the cookie in the request header.
    

## Contributing

Contributions are welcome! Feel free to submit PRs or open issues.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
