FROM docker.io/library/maven:3.9.4-eclipse-temurin-17

RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group spring-boot
USER spring-boot:spring-boot-group

WORKDIR /build

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
