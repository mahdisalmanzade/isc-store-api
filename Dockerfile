# Use a lightweight JDK 21 image
FROM eclipse-temurin:21-jdk

# Set a working directory
WORKDIR /app

# Copy the jar file from target folder into the container
COPY target/*.jar app.jar

# Expose the port Spring Boot runs on (default 8080)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
