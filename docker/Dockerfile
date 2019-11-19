# This is the Dockerfile reference for Spring based microservices. Multi-Stage feature is used to build image.
#
# REQUIRED FILES TO BUILD THIS IMAGE
# ----------------------------------
# (1) JAR file containing the microservice compiled code.
 
# Builder Image
#FROM docker-sd.faas.securitasdirect.local/openjdk:8-jdk-alpine as build
FROM openjdk:8-jdk-alpine as build
# Required build arguments
ARG NAME
ARG VERSION
WORKDIR /workspace/app
COPY $NAME-$VERSION.jar $NAME.jar
RUN jar -xf $NAME.jar
 
 
# Base Image
#FROM docker-sd.faas.securitasdirect.local/openjdk:8-jre-alpine
FROM openjdk:8-jre-alpine
# Maintainer
LABEL maintainer="FaaS <faas@securitasdirect.es>"
# Required build arguments
ARG NAME
VOLUME /tmp
# Create verisure group and user and give permissions over logs folder.
RUN addgroup verisure \
&& adduser -u 1000 -s /bin/sh -D -G verisure verisure \
&& mkdir /logs \
&& chown verisure:verisure /logs \
&& chmod a+rw /logs
USER verisure
# Set environment variables
ENV ARTIFACT_NAME=$NAME \
SERVER_PORT=8080 \
MAX_RAM_PERCENTAGE=75.0 \
SPRING_PROFILES_ACTIVE=cloud \
JAVA_TOOL_OPTIONS=-Duser.timezone="Europe/Madrid" \
TZ=CET-1CEST-2,M3.5.0/2,M10.5.0/3
# Expose Service
EXPOSE ${SERVER_PORT} ${MANAGEMENT_PORT}
# Copy classes from builder image
ARG DEPENDENCY=/workspace/app/
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build ${DEPENDENCY}/org/ /app
# Entry point
 
ENTRYPOINT java -XX:+UseG1GC -XX:MaxRAMPercentage=$MAX_RAM_PERCENTAGE \
-Djava.security.egd=file:/dev/./urandom -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap \
-cp app:app/lib/* com.msl.cache.springcachemulti.Application -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE