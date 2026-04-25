# UmuWork API 🔧

A production-grade REST API for connecting informal skilled workers
with clients in Rwanda. Built with Spring Boot, PostgreSQL, and MTN MoMo.

## Tech Stack
- Java 21
- Spring Boot 3.3.5
- Spring Data JPA + Hibernate
- PostgreSQL
- Lombok
- Maven

## Features
- Worker profile creation and verification
- Job posting and application system
- Rating and review system
- MTN MoMo payment integration (in progress)

## Project Structure
src/main/java/com/umuwork/
├── model/          # Database entities
├── dto/            # Request and Response DTOs
│   ├── request/
│   └── response/
├── enums/          # Fixed value types
├── repository/     # Database queries
├── service/        # Business logic
├── controller/     # API endpoints
├── exception/      # Global error handling
└── config/         # App configuration

## Getting Started

### Prerequisites
- Java 21
- PostgreSQL
- Maven

### Setup
1. Clone the repo
```bash
   git clone https://github.com/yourname/umuwork-api.git
```

2. Create the database
```sql
   CREATE DATABASE umuwork_db;
```

3. Update `application.properties`
```properties
   spring.datasource.username=youruser
   spring.datasource.password=yourpassword
```

4. Run the app
```bash
   mvn spring-boot:run
```

## API Endpoints (coming soon)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register a user |
| POST | `/api/auth/login` | Login |
| GET | `/api/workers/search` | Search workers |
| POST | `/api/jobs` | Post a job |
| POST | `/api/jobs/{id}/apply` | Apply to a job |

## Author
Built for the Rwandan informal labor market 🇷🇼