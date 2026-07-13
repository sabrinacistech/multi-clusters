FROM eclipse-temurin:21-jre-ubi9-minimal

WORKDIR /app

COPY target/active-cluster-service-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
