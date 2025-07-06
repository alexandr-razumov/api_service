# Многоэтапная сборка
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Финальный образ
FROM openjdk:17-jdk-slim
WORKDIR /app

# Установить curl для health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Скопировать JAR файл
COPY --from=build /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/status || exit 1

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
