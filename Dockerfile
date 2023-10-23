FROM maven:3.8.6-openjdk-18 AS build

WORKDIR /build
COPY pom.xml pom.xml
RUN mvn verify --fail-never
COPY . .
RUN mvn package -Dmaven.test.skip

FROM openjdk:18-jdk

WORKDIR /java
COPY --from=build /build/target/*.jar /java/java.jar

RUN chown -R 1001:1001 /java \
    && chmod -R g=u /java

USER 1001
EXPOSE 8080
CMD ["java", "-jar", "/java/java.jar"]
