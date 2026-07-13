# ---- build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B clean package -DskipTests

# ---- runtime stage ----
FROM eclipse-temurin:21-jre-ubi9-minimal
WORKDIR /app
COPY --from=build /workspace/target/cluster-status-monitor-*.jar /app/app.jar
EXPOSE 8081
USER 1001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
