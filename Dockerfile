# Etapa de compilación usando Maven
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar --no-daemon

# Etapa de ejecución ligera
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto por defecto (Render inyectará la variable PORT)
EXPOSE 8080

# Comando para ejecutar la aplicación limitando la memoria de la JVM (pedido en la rúbrica)
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
