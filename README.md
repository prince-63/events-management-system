### 🎉 Events Management System - Spring Boot Project

A production-grade Events Management backend system built with Java, Spring Boot, and MySQL. It allows users to register, log in securely, register for events, and check in via QR codes. Organizers and admins can create and manage events, view attendees, and control access securely using role-based permissions.

This project reflects real-world software development practices and demonstrates production-ready backend engineering.

#### Tech Stack

* Java 21, Spring Boot 3
* Spring Security with JWT
* Spring Data JPA (Hibernate)
* MySQL, Maven
* JavaMailSender (for email notifications)
* Cloudinary (for banner/image uploads)
* ZXing (for QR code generation)
* Swagger (OpenAPI documentation)

#### Authentication and Authorization

* Secure login using JWT tokens
* Role-based access control (`USER`, `ORGANIZER`, `ADMIN`)
* Custom logout and authentication filter handling

#### API Documentation

* Swagger UI for testing and viewing all available APIs
* URL: `http://localhost:8080/swagger-ui/index.html`
* Generated using springdoc-openapi with proper schema and error responses

#### Core Features

* User registration and login
* Create, update, and delete events
* Event registration by authenticated users
* Email confirmation after registration
* QR code generation and check-in flow
* Admin controls for managing all data

#### Modules

* User
* Event
* Registration
* QRCodeToken

Code is structured under:

* `controllers`
* `services`
* `repositories`
* `dto`
* `entity`
* `constants`
* `filters`
* `mapper`
* `config`
* `security`
* `exception`
* `utils`

### How to Run Locally

```bash
mvn clean install
mvn spring-boot:run
```
