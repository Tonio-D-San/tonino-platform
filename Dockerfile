FROM maven:3-amazoncorretto-25-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package

FROM amazoncorretto:25-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar alarion.jar

ENTRYPOINT ["java", "-jar", "platform.jar"]