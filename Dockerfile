# Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy all files into container
COPY . .

# Build the project using Maven wrapper
RUN ./mvnw clean install -DskipTests

# Run the Spring Boot app
CMD ["./mvnw", "spring-boot:run"]
