# This is the Dockerfile reference for Spring based microservices
# 
# REQUIRED FILES TO BUILD THIS IMAGE
# ----------------------------------
# (1) JAR file containing the microservice compiled code.
#
# Pull base image
FROM openjdk:8-jre-alpine

# Maintainer
# ----------
LABEL maintainer="Miguel Salal <msalaslo@gmail.com>"

VOLUME /tmp


# Create a group and user and give permissions over logs folder.
RUN addgroup cache \
    && adduser -u 1000 -s /bin/sh -D -G cache cache \
    && mkdir /logs \
    && chown cache:cache /logs \
    && chmod a+rw /logs
USER cache

# Required build arguments
ARG NAME
ARG VERSION

# Set environment variables
ENV ARTIFACT_NAME=$NAME \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=cloud \
    JAVA_TOOL_OPTIONS=-Duser.timezone="Europe/Madrid" \
    TZ=CET-1CEST,M3.5.0,M10.5.0/3


EXPOSE ${SERVER_PORT} ${MANAGEMENT_PORT}

#Add EXECUTABLE JAR
COPY docker/$NAME-$VERSION.jar $NAME.jar

#Add external file
#ADD path/file file

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -XX:+PrintFlagsFinal -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /$ARTIFACT_NAME.jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE
