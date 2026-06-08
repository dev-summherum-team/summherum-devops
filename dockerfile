// Erstellt Dockerfile mit Linux, Java und jre. Öffnet erstellte JAR, startet Anwendung
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
