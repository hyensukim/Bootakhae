FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/config-server-0.0.1-SNAPSHOT.jar config-server.jar
ENTRYPOINT ["java", "-jar", "config-server.jar"]