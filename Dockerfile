# Fase 1: Construcción
FROM eclipse-temurin:23-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests


# Fase 2: Imagen final
FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]