FROM maven:3.9.11-eclipse-temurin-17 AS compilacion

WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw.cmd ./
COPY src src

RUN mvn -DskipTests package

FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=compilacion /app/target/fixcampus-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_SQL_INIT_MODE=always \
    SPRING_SQL_INIT_DATA_LOCATIONS=classpath:roles-iniciales.sql \
    SPRING_JPA_DEFER_DATASOURCE_INITIALIZATION=true

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
