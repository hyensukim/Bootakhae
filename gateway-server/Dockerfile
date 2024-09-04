FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/gateway-server-0.0.1-SNAPSHOT.jar gateway-server.jar
ENTRYPOINT ["java", "-jar", "gateway-server.jar"]