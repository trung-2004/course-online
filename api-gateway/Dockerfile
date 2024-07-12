FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y maven
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
EXPOSE 8888
ENTRYPOINT [ "java", "-jar", "/home/app/target/api-gateway-0.0.1-SNAPSHOT.jar" ]