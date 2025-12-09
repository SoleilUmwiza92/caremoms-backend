# ================================
# 1. BUILD STAGE
# ================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

# Copy only pom.xml to download dependencies first (leveraging Docker cache)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code and build the JAR
COPY src ./src
RUN mvn -B clean package -DskipTests

# ================================
# 2. RUNTIME STAGE
# ================================
FROM eclipse-temurin:17-jre-alpine

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy JAR from the build stage
COPY --from=build /workspace/target/*.jar ./app.jar

# Ensure the JAR is owned by the non-root user
RUN chown appuser:appgroup ./app.jar

# Switch to non-root user
USER appuser

# Expose backend port
EXPOSE 8080

# JVM options: container-aware settings and tuning for development
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"
# Activate Spring profile (override via environment)
ENV SPRING_PROFILES_ACTIVE=docker

# Launch the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
