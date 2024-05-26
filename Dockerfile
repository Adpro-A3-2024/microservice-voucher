# Use a multi-stage build to reduce the final image size
# Stage 1: Build the application
FROM gradle:8.0.2-jdk21 as builder
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build -x test

# Stage 2: Package the application
FROM openjdk:21-jre-slim
VOLUME /tmp
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

