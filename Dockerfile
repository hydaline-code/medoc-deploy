# Use Eclipse Temurin OpenJDK 17 runtime image (lightweight and reliable)
FROM eclipse-temurin:17-jre

# Create and set working directory inside the container
WORKDIR /app

# Copy your Spring Boot fat JAR into the container
COPY target/Medoc-0.0.1-SNAPSHOT.jar app.jar

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
