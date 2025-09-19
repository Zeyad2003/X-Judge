# X-Judge

X-Judge is an open-source competitive programming platform that aggregates problems from multiple online judges (OJs) into a unified interface. Inspired by platforms like vjudge.net, X-Judge streamlines the experience for practicing, competing, and learning by centralizing problem access, submissions, and contest management.

## Key Features

- **Unified Problem Aggregation**: Search and solve problems from various OJs (e.g., Codeforces, AtCoder) in one place.
- **Centralized Submissions**: Submit solutions to supported OJs directly from X-Judge.
- **Contest Management**: Create, join, and manage virtual contests using problems from different OJs.
- **Group Collaboration**: Form groups, practice together, and track collective progress.
- **User Progress Tracking**: Monitor individual and group achievements and statistics.

## Technology Stack

- **Backend**: Java 21, Spring Boot
- **Build Tool**: Gradle
- **Database**: MySQL
- **Containerization**: Docker, Docker Compose
- **API Documentation**: Swagger (OpenAPI)
- **Web Scraping**: Jsoup (fetching), PlayWright (submitting)
- **Object Mapping**: MapStruct
- **DB Migration**: Flyway

## Project Structure

```
.
├── build.gradle.kts
├── docker-compose.yml
├── Dockerfile
├── src
│   ├── main
│   │   ├── java/com/xjudge/... (source code)
│   │   └── resources
│   └── test
└── ...
```

## Getting Started

### Prerequisites

- Java 21
- Docker & Docker Compose
- MySQL (if running locally without Docker Compose)

### Build

Build the application using the Gradle wrapper:

```bash
./gradlew build
```

### Run

You can run X-Judge in two ways:

#### 1. Using Docker Compose (Recommended for production/testing)
This will start the application and a MySQL database. The application will be available at `http://localhost:7070`.

```bash
docker-compose up --build
```

#### 2. Using Spring Boot (Development)
Run the application directly using Gradle. The application will be available at `http://localhost:8080`.

```bash
./gradlew bootRun
```

### API Documentation

Swagger UI is available at:
- `http://localhost:7070/swagger-ui/index.html` (Docker Compose)
- `http://localhost:8080/swagger-ui/index.html` (bootRun)

## Roadmap

- **Phase 1: Problem Fetching (In Progress)**
    - Fetch problems from Codeforces, AtCoder, and other OJs
    - Add comprehensive unit and integration tests
    - Set up database migrations (Flyway/Liquibase)
- **Phase 2: Submission Handling**
    - Implement submission service for code submissions to OJs
- **Phase 3: Contest & Group Management**
    - Features for creating/managing contests and user groups
- **CI/CD**
    - Automated testing, building, and Docker image publishing

## Contributing

Contributions are welcome! Please open an issue or submit a pull request to help improve X-Judge.

---

**Note:**
- Default port is **7070** when running with Docker Compose.
- Default port is **8080** when running with `bootRun`.
- The application uses **MySQL** as its database.
