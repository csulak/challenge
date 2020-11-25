FROM gradle:6.7.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon
FROM adoptopenjdk/openjdk13:alpine-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/demo-docker-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev", "/app/demo-docker-0.0.1-SNAPSHOT.jar"]