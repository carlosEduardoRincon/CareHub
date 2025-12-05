FROM maven:3.9.9-eclipse-temurin-24 AS builder
COPY . /app
WORKDIR /app

RUN --mount=type=cache,target=/root/.m2 mvn -T1C -B -DskipTests=true -pl Scheduling -am clean package

FROM eclipse-temurin:24-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app
COPY --from=builder /app/Scheduling/target/*.jar /app/app.jar

FROM eclipse-temurin:17.0.11_9-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
