FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/schedule-service-0.0.1-SNAPSHOT.jar schedule-service.jar
ENTRYPOINT ["java", "-jar", "schedule-service.jar"]