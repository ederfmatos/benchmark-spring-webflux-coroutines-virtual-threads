FROM gradle:8.7-alpine as builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]