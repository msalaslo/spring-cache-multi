# This is the Dockerfile 
#
# REQUIRED FILES TO BUILD THIS IMAGE
# ----------------------------------
# (1) JAR file containing the microservice compiled code.
 
# Builder Image
FROM openjdk:8-jdk-alpine as build
# Required build arguments
ARG NAME
ARG VERSION
WORKDIR /workspace/app
COPY $NAME-$VERSION.jar $NAME.jar
RUN jar -xf $NAME.jar
 
 
# Base Image
FROM openjdk:8-jre-alpine
# Required build arguments
ARG NAME
VOLUME /tmp
# Create imguser group and user and give permissions over logs folder.
RUN addgroup imguser \
&& adduser -u 1000 -s /bin/sh -D -G imguser imguser \
&& mkdir /logs \
&& chown imguser:imguser /logs \
&& chmod a+rw /logs
USER imguser
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