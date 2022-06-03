FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} api_toche_web.jar
COPY /toche_api/private/application-real.yml application-real.yml

ENTRYPOINT ["java", "-Dspring.profile.active=real", "-Dspring.config.location=classpath:/application.yml,/application-real.yml", "-jar", "api_toche_web.jar"]