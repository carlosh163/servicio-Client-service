FROM openjdk:8
VOLUME /tmp
ADD ./target/servicio-PersonalClient-service-0.0.1-SNAPSHOT.jar servicio-client.jar
ENTRYPOINT ["java","-jar","/servicio-client.jar"]