FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} electronic-app.jar
ENTRYPOINT ["java", "-jar", "/electronic-app.jar"]