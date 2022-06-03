FROM openjdk:11

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} api_toche_web.jar

ENTRYPOINT ["java", "-Dspring.profile.active=real", "-jar", "api_toche_web.jar"]