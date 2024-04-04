# syntax=docker/dockerfile:1

FROM maven:3.9.6-eclipse-temurin-22 AS base

FROM base AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package


FROM base AS final

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
