# ========== Stage 1: Build ==========
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# --- STEP 1: Copy pom.xml & download dependencies (cache layer)
COPY pom.xml .

# Fake src directory to skip maven resolve dependency
RUN mkdir -p src/main/java
RUN mvn -q dependency:go-offline

# --- STEP 2: Copy real src directory
COPY src ./src

# Build project
RUN mvn clean package -DskipTests


# ========== Stage 2: Runtime ==========
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
