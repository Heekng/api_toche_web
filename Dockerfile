FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} api_toche_web.jar
COPY /private/application-real.yml application-real.yml
COPY /private/application-private.yml application-private.yml

ENTRYPOINT ["java", "-Dspring.profiles.active=real", "-Dspring.config.location=classpath:/application.yml,/application-real.yml,/application-private.yml", "-jar", "api_toche_web.jar"]