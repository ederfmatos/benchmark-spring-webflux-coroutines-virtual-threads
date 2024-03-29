FROM gradle:8.7-jdk21 as builder
WORKDIR /app
COPY . .
RUN gradle build -x test

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]