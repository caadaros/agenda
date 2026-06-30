# ── Etapa 1: compilar ──────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copiar archivos de Maven
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Descargar dependencias primero (capa cacheada)
RUN ./mvnw dependency:go-offline -q

# Copiar código fuente y compilar sin ejecutar tests
COPY src ./src
RUN ./mvnw package -DskipTests -q

# ── Etapa 2: imagen final ligera ───────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar solo el JAR generado
COPY --from=builder /app/target/*.jar app.jar

# Render asigna el puerto dinámicamente vía variable PORT
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]