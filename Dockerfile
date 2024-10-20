# Use the official OpenJDK image as the base image
FROM eclipse-temurin:23-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradlew /app/
COPY gradle /app/gradle
COPY build.gradle /app/
COPY settings.gradle /app/

# Copy the source code
COPY src /app/src

# Grant execution rights to the Gradle wrapper
RUN chmod +x gradlew

# Build the application using the bootJar task
RUN ./gradlew bootJar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/brokeragefirm-0.0.1-SNAPSHOT.jar"]
