# Etapa de build: usamos Eclipse Temurin 21 y el Maven Wrapper (mvnw)
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiamos los archivos necesarios para que mvnw funcione
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Damos permisos de ejecución a mvnw y construimos el JAR
RUN chmod +x mvnw && ./mvnw -q clean package -DskipTests

# Etapa de runtime: solo Java 21 y el JAR ya construido
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 8080 (lo que te piden)
EXPOSE 8080

# Perfil por defecto dentro del contenedor
ENV SPRING_PROFILES_ACTIVE=dev

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
