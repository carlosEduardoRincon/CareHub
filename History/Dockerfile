FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . /app

RUN --mount=type=cache,target=/root/.m2 mvn -T1C -B -DskipTests=true -pl History -am clean package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app
COPY --from=builder /app/History/target/*.jar /app/app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/app.jar"]