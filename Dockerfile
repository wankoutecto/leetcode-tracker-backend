
# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

#Expose the app port (default spring boot is 8080)
EXPOSE 8080

#Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]