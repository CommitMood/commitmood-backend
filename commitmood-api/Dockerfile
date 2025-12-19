# 멀티 빌드
FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY settings.gradle build.gradle ./
COPY ../commitmood-common/ ./commitmood-common
COPY ../commitmood-api/ ./commitmood-api
RUN gradle :commitmood-api:bootJar --no-daemon

FROM eclipse-temurin:21-jdk
COPY --from=builder /app/commitmood-api/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]