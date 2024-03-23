FROM openjdk:14-jdk-alpine
MAINTAINER upgrad
ADD ./target/appointment-service.jar /usr/BMC-App/appointment-service.jar
WORKDIR /usr/BMC-App
ENV PATH="${PATH}:${JAVA_HOME}/bin"
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/usr/BMC-App/appointment-service.jar"]