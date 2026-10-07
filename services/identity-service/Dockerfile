# syntax=docker/dockerfile:1.7
FROM maven:3-amazoncorretto-25-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN --mount=type=secret,id=maven_settings,target=/root/.m2/settings.xml \
    mvn clean package -DskipTests

FROM amazoncorretto:25-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
