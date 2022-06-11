FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} api_toche_web.jar
COPY /private/application-real.yml application-real.yml
COPY /private/application-private.yml application-private.yml

ENTRYPOINT ["java", "-Dspring.profile.active=real", "-Dspring.config.location=classpath:/application.yml,/application-real.yml,/application-private", "-jar", "api_toche_web.jar"]