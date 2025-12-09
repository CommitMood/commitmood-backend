FROM eclipse-temurin:21-jdk
LABEL authors="yeseong"

ARG JAR_FILE=commitmood-api/build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]