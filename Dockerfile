FROM openjdk:13-jdk-alpine 

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
RUN apk add --no-cache bash
COPY ./build/libs/* ./app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]