# Etapa de compilación usando Maven/Gradle
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .

# ---> ESTA ES LA LÍNEA NUEVA: Le damos permiso de ejecución al wrapper de Gradle <---
RUN chmod +x gradlew

# Ahora sí, compila sin problemas de permisos
RUN ./gradlew clean bootJar --no-daemon

# Etapa de ejecución ligera
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto por defecto
EXPOSE 8080

# Comando para ejecutar la aplicación limitando la memoria de la JVM
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]