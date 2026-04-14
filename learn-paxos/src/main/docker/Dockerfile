FROM docker.m.daocloud.io/eclipse-temurin:17-jdk
VOLUME /tmp
WORKDIR /app
COPY learn-paxos-0.0.1-SNAPSHOT.jar /app/learn-paxos.jar
EXPOSE 8081
ENTRYPOINT ["java" , "-jar", "/app/learn-paxos.jar"]