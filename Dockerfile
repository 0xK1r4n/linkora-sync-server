FROM gradle:latest AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle.* gradle.properties /home/gradle/app/
COPY gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app
RUN gradle clean build -i --stacktrace

FROM gradle:latest AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/app/
WORKDIR /usr/src/app
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM amazoncorretto:23-alpine AS runtime
RUN apk add --no-cache sqlite
RUN mkdir -p /data
ENV LINKORA_SERVER_USE_ENV_VAL=true \
    LINKORA_DATABASE_URL=sqlite:///data/linkora_db \
    LINKORA_HOST_ADDRESS=0.0.0.0 \
    LINKORA_SERVER_PORT=8080
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/linkoraSyncServer.jar
ENTRYPOINT ["java","-jar","/app/linkoraSyncServer.jar"]