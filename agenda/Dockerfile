# ── Etapa 1: compilar ──────────────────────────────────────────
# Usamos el Maven de la imagen oficial (no dependemos de mvnw,
# evitando problemas de permisos/CRLF al subir desde Windows)
# Descarga maven y lo guarda como builder
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
# Crea la carpeta /app y la establece como directorio de trabajo
WORKDIR /app

# Copia el pom en ./app
COPY pom.xml ./
# Descarga las dependencias desde internet 
RUN mvn dependency:go-offline -q

# Copiar el código fuente 
COPY src ./src
# Limpia compilaciones anteriores, compila el código fuente y lo guarda en un .jar,
# omite los test y omite mensajes en consola para que log quede limpio
RUN mvn clean package -DskipTests -q

# ── Etapa 2: imagen final ligera ───────────────────────────────
# Desde el ditribuidor usa java 21 imagen JRE (sólo contiene los necesario), 
# del sistema operativo Alpine Linux
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# instala wget borrando archivos temporales (wge es necesario para revisar si el ms sigue funcionado)
RUN apk add --no-cache wget

# destro de buider copia el.jar (indica ruta) y lo pega en app.jar
# sirve para dejar atrás la basura y sólo dejar el .jar final
COPY --from=builder /app/target/*.jar app.jar
COPY Wallet_* ./Wallet/

#Escucha y recibe peticiones en el puerto 8080
EXPOSE ${PORT}

# ejecuta el siguiente comando cuando se encienda (entorno, parámetro, archivo final)
ENTRYPOINT ["java", "-jar", "app.jar"]
