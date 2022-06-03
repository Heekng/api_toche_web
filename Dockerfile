FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} api_toche_web.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profile.active=real", "-Dspring.config.location=classpath:/application.yml,/toche_api/private/application-real.yml", "api_toche_web.jar"]