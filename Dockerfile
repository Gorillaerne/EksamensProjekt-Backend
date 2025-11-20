FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
# Hvis en dependency ændrer sig i pom.xml, smides
# download af dependencies ud og man starter forfra.
COPY pom.xml .
RUN mvn dependency:resolve
# Hvis en ændring sker i kildekoden, men ikke i pom.xml
# bevares dependencies i cache'en, så det kun er koden
# der compiles igen; dependencies downloades ikke igen
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]