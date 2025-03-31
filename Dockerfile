FROM gradle:alpine AS cache
ENV GRADLE_USER_HOME /home/gradle/.gradle
COPY build.gradle.kts gradle.properties settings.gradle.kts /home/gradle/src/
COPY gradle/wrapper /home/gradle/src/gradle/wrapper
WORKDIR /home/gradle/src
RUN gradle --no-daemon dependencies

# ----------------------------

FROM gradle:alpine AS build
COPY --from=cache /home/gradle/.gradle /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --no-daemon --build-cache buildFatJar

# ----------------------------

FROM amazoncorretto:23-alpine AS runtime
RUN apk add --no-cache sqlite && \
    mkdir -p /data
ENV LINKORA_SERVER_USE_ENV_VAL=true \
    LINKORA_DATABASE_URL=sqlite:///data/linkora_db \
    LINKORA_HOST_ADDRESS=0.0.0.0 \
    LINKORA_SERVER_PORT=8080
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar /app/linkoraSyncServer.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/linkoraSyncServer.jar"]