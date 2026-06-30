# ── Etapa 1: compilar ──────────────────────────────────────────
# Usamos el Maven de la imagen oficial (no dependemos de mvnw,
# evitando problemas de permisos/CRLF al subir desde Windows)
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copiar solo el pom primero (capa cacheada de dependencias)
COPY pom.xml ./
RUN mvn dependency:go-offline -q

# Copiar el código fuente y compilar sin tests
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Etapa 2: imagen final ligera ───────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# wget es necesario para el healthcheck definido en docker-compose
RUN apk add --no-cache wget

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
