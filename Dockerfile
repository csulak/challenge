FROM openjdk:13-jdk-alpine

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
RUN apk add --no-cache bash
COPY ./build/libs/* ./app.jar
COPY ./wait-for-it.sh ./wait-for-it.sh
EXPOSE 8080
CMD ["./wait-for-it.sh", "db:3306", "--", "java","-jar","app.jar"]