# ============================================================
# 1. BUILD STAGE — uses full JDK & Maven
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS build

# Use a non-root workspace
WORKDIR /workspace

# Copy only the POM first (maximizes Docker cache efficiency)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Copy source code
COPY src ./src

# Build the application (skip tests for faster CI)
RUN mvn -B -q clean package -DskipTests


# ============================================================
# 2. RUNTIME STAGE — ultra-light, secure JRE
# ============================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# ===========================
# SECURITY HARDENING
# ===========================
# Install minimal dependencies only
RUN apk add --no-cache curl

# Create non-root user/group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the JAR from the container build stage
COPY --from=build /workspace/target/*.jar /app/app.jar

# Adjust file permissions
RUN chown -R appuser:appgroup /app

# Switch to non-root user for security
USER appuser

# ===========================
# RUNTIME CONFIGURATION
# ===========================
EXPOSE 8080

# JVM Flags:
# - Container-aware sizing
# - Disable biased locking
# - Fast entropy for secure random
# - Prefer IPv4 (works better inside containers)
ENV JAVA_OPTS="\
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75 \
  -Djava.security.egd=file:/dev/./urandom \
  -Djava.net.preferIPv4Stack=true \
"

# Spring Boot active profile
ENV SPRING_PROFILES_ACTIVE=docker

# ===========================
# HEALTHCHECK
# Best practice – ensures Railway/Vercel/Docker detect crashes
# ===========================
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -fs http://localhost:8080/actuator/health || exit 1

# ===========================
# ENTRYPOINT
# ===========================
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
