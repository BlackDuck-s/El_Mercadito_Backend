# Etapa 1: build con Maven Wrapper
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamos Maven Wrapper y pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Damos permiso de ejecución al wrapper (Linux)
RUN chmod +x mvnw

# Descargamos dependencias para cachear
RUN ./mvnw -q -B dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Empaquetamos la app (sin tests para que sea más rápido)
RUN ./mvnw -q -B package -DskipTests

# Etapa 2: imagen final
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
