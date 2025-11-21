# ================================
# 1. BUILD STAGE
# ================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml separately to leverage Docker cache for dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code and build JAR
COPY src ./src
RUN mvn -B clean package -DskipTests

# ================================
# 2. RUNTIME STAGE
# ================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Add a non-root user (security best practice)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Set permissions
RUN chown appuser:appgroup app.jar

USER appuser

# Expose backend port
EXPOSE 8080

# Use a more REST-friendly default JVM options (optional)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run Spring Boot app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
