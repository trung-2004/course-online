FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y maven
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
EXPOSE 8762
ENTRYPOINT [ "java", "-jar", "/home/app/target/registry-service-0.0.1-SNAPSHOT.jar" ]