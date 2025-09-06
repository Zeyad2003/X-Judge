FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY gradlew ./
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
