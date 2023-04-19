FROM openjdk:11-jre-slim
VOLUME /tmp
ADD *.jar das.jar
COPY *.jar das.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/das.jar","--spring.profiles.active=prod"]