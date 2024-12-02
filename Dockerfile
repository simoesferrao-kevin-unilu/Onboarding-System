# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled .jar file (assuming you're using Maven and it generates the jar in the target folder)
COPY target/onboarding-system-1.0-SNAPSHOT.jar /app/onboarding-system-1.0-SNAPSHOT.jar

# Expose the port your app will run on (optional)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/onboarding-system-1.0-SNAPSHOT.jar"]