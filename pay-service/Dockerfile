FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/pay-service-0.0.1-SNAPSHOT.jar pay-service.jar
ENTRYPOINT ["java", "-jar", "pay-service.jar"]