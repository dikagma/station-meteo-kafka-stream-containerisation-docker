# Étape 1 : image minimaliste avec Java 17
FROM eclipse-temurin:17-jdk-jammy

# Définir un dossier de travail
WORKDIR /app

# Copier le JAR dans l'image
COPY target/weather-kafka-streams-app-1.0-SNAPSHOT.jar app.jar


# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
