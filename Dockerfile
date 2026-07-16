# Building executable .jar from the files
FROM maven:3.9.14-eclipse-temurin-21-alpine AS builder

WORKDIR /builder

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package spring-boot:repackage -Dmaven.test.skip=true

# Running the code
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /builder/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]