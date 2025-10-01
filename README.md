# isc-store-api
I've built a store app with JWT Spring Boot, authentication, Docker, H2 database, and Docker support.

ISC Store API

A Spring Boot-based RESTful API for managing an online store. Supports user authentication with JWT, product and cart management, and easy testing with Swagger UI. Dockerized for easy deployment.

Features

User registration and login with JWT authentication

Product and cart management (CRUD operations)

H2 in-memory database for development

API documentation using Swagger UI

Secure endpoints with Spring Security

Docker support for easy deployment

Technology Stack

Java 21

Spring Boot 3

Spring Security with JWT

H2 Database

Maven

Swagger (OpenAPI)

Docker

Getting Started
Prerequisites

Java 21

Maven 3.x

Docker (optional, for containerized deployment)

Clone the repository
git clone https://github.com/mahdisalmanzade/isc-store-api.git
cd isc-store-api

Running locally

Build the project:

mvn clean install


Run the application:

mvn spring-boot:run


Access the API:

Swagger UI: http://localhost:8080/swagger-ui/index.html

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

Username: sa

Password: (leave empty)

Environment Variables

Make sure to set your JWT secret in application.properties or application.yml:

jwt.secret=your_secret_key_here


Replace your_secret_key_here with a strong, random secret string.

Docker

Build Docker image:

docker build -t isc-store-api .


Run container:

docker run -p 8080:8080 isc-store-api

Authentication

Register a new user via:

POST /users


Log in via:

POST /auth/login


The response will return a JWT token. Use this token in the Authorization header for all protected endpoints:

Authorization: Bearer <your_token_here>

API Endpoints

/users – Register new users

/auth/login – Login and receive JWT token

/products – CRUD operations for products

/carts – Manage shopping cart

/h2-console – H2 database console

/swagger-ui/index.html – API documentation

License

This project is licensed under the MIT License.