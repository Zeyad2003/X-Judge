# X-Judge Development Plan

## Introduction

This document outlines a detailed, phased development plan to transform the X-Judge project from its current state into a robust, production-ready application. This plan is designed to be followed by a junior developer, with clear, actionable steps.

---

## Phase 0: Project Stabilization & Basic Setup

**Goal:** Get the application into a stable, runnable, and easily testable state. This phase addresses the most critical issues that prevent reliable development and testing.

### 1. **Establish a Consistent Development Environment**
- **Goal:** Ensure the project can be reliably run by any developer.
- **Tasks:**
    - **1.1. Dockerize the Database:** Create a `docker-compose.yml` file to run a MySQL container. This removes the need for each developer to install and configure their own MySQL instance.
    - **1.2. Create a Local `env.properties` File:** Create a template `env.properties.example` file with placeholder values. Each developer can then create their own `env.properties` file for local development.
    - **1.3. Update README:** Update the `README.md` with clear, step-by-step instructions on how to set up the development environment using Docker and the `env.properties` file.

### 2. **Fix Critical Security Vulnerabilities**
- **Goal:** Address the most pressing security issues.
- **Tasks:**
    - **2.1. Implement Authorization Checks:** In `GroupServiceImpl`, add authorization checks to ensure that only group leaders or admins can perform sensitive actions (e.g., deleting the group, removing members).
    - **2.2. Refactor Credential Management:** Move the hardcoded CodeForces and AtCoder credentials from `application.properties` to a more secure solution. For now, we can use environment variables, but we should plan to use a proper secret manager in the future.

### 3. **Address Incomplete Features**
- **Goal:** Complete the partially implemented features to make the application more functional.
- **Tasks:**
    - **3.1. Complete `UserGroupServiceImpl`:** Implement the logic to get groups where the user has the role of `LEADER` or `ADMIN`.

---

## Phase 1: Foundation & Stability

**Goal:** Strengthen the core of the application to ensure it is reliable, maintainable, and easy to work with.

### 1. **Comprehensive Testing**
- **Goal:** Ensure the correctness of the existing features and prevent regressions.
- **Tasks:**
    - **1.1. Unit Tests:** Write JUnit tests for all public methods in the service and controller layers. Use Mockito to mock dependencies.
    - **1.2. Integration Tests:** Write integration tests for the main API endpoints. Use a test-specific database configuration (e.g., an in-memory H2 database or a separate test database).
    - **1.3. CI Pipeline:** Set up a basic CI pipeline using GitHub Actions to automatically run all tests on every push to the main branch.

### 2. **Robust Error Handling**
- **Goal:** Make the application resilient to errors, especially from external services.
- **Tasks:**
    - **2.1. Global Exception Handler:** Implement a global exception handler (`@ControllerAdvice`) to catch unhandled exceptions and return consistent, user-friendly error responses.
    - **2.2. External Service Resilience:** For services that interact with external platforms (e.g., Codeforces, AtCoder), implement resilience patterns:
        - **Retries:** Use a library like Spring Retry to automatically retry failed requests.
        - **Circuit Breaker:** Use a library like Resilience4j to prevent cascading failures when an external service is down.
        - **Fallbacks:** Provide sensible fallback behavior (e.g., returning cached data) when an external service is unavailable.

### 3. **Database Migration with Flyway**
- **Goal:** Manage database schema changes in a safe, version-controlled way.
- **Tasks:**
    - **3.1. Integrate Flyway:** Add the Flyway dependency to the `pom.xml` (or `build.gradle` after migration).
    - **3.2. Disable `ddl-auto`:** Set `spring.jpa.hibernate.ddl-auto` to `validate` in `application.properties`.
    - **3.3. Create Initial Migration:** Create an initial Flyway migration script that reflects the current database schema.
    - **3.4. Enforce Migration-Based Schema Changes:** Establish a rule that all future database schema changes must be made through new Flyway migration scripts.

---

## Phase 2: Build System & Performance

**Goal:** Modernize the build system and improve the performance and scalability of the application.

### 1. **Migrate from Maven to Gradle**
- **Goal:** Move to a more modern, flexible, and performant build tool.
- **Tasks:**
    - **1.1. Create `build.gradle`:** Create a `build.gradle` file and translate all dependencies and build configurations from `pom.xml`.
    - **1.2. Remove `pom.xml`:** Once the Gradle build is working correctly, delete the `pom.xml` file.
    - **1.3. Update README:** Update the `README.md` with instructions on how to build and run the project using Gradle.

### 2. **Caching with Redis**
- **Goal:** Improve response times for frequently accessed data.
- **Tasks:**
    - **2.1. Integrate Redis:** Add the Spring Data Redis dependency and configure the connection to a Redis instance (which can also be run in Docker).
    - **2.2. Cache Expensive Operations:** Identify and cache data that is expensive to compute or fetch, such as:
        - Contest rankings
        - Scraped problem data
        - User profiles

### 3. **Asynchronous Task Processing with RabbitMQ**
- **Goal:** Decouple long-running tasks from the main application thread to improve responsiveness.
- **Tasks:**
    - **3.1. Integrate RabbitMQ:** Add the Spring AMQP dependency and configure the connection to a RabbitMQ instance (also in Docker).
    - **3.2. Offload Long-Running Tasks:** Move the following tasks to a message queue:
        - Code judging and submission processing
        - Sending emails

---

## Phase 3: Deployment & DevOps

**Goal:** Make the application easy to deploy, manage, and scale.

### 1. **Containerization with Docker**
- **Goal:** Package the application and its dependencies into containers for consistent and portable deployments.
- **Tasks:**
    - **1.1. Create a `Dockerfile`:** Write a `Dockerfile` to build a container image for the Spring Boot application.
    - **1.2. Enhance `docker-compose.yml`:** Update the `docker-compose.yml` file to include the application container, along with the database, Redis, and RabbitMQ containers.

### 2. **CI/CD Pipeline**
- **Goal:** Automate the build, test, and deployment process.
- **Tasks:**
    - **2.1. Set up GitHub Actions:** Create a GitHub Actions workflow to:
        - Build the application
        - Run all tests
        - Build the Docker image
        - Push the Docker image to a container registry (e.g., Docker Hub, GitHub Container Registry)
        - (Optional) Deploy to a staging environment.

---

## Phase 4: Advanced Features

**Goal:** Add new features to distinguish the platform and provide more value to users.

### 1. **Cheating Detection**
- **Goal:** Implement a system to detect cheating in submissions.
- **Tasks:**
    - **1.1. Research:** Research different cheating detection techniques, such as code similarity analysis (e.g., using MOSS) or leveraging Large Language Models (LLMs).
    - **1.2. Design and Implement:** Design and implement a cheating detection system based on the research. This could be a complex, long-term project.

### 2. **Improve User Profiles**
- **Goal:** Enhance user profiles with more statistics and visualizations.
- **Tasks:**
    - **2.1. Add More Stats:** Track and display more user statistics, such as submission history, verdict counts, and problem-solving heatmaps.
    - **2.2. Add Visualizations:** Use a charting library (e.g., Chart.js) to create visualizations of user progress.