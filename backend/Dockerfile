FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
COPY src/main/resources/application.properties app/application.properties

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]