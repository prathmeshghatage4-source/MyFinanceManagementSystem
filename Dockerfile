# Use Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the built jar file into container
COPY target/*.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]