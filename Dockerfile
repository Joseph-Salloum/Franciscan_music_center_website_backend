FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn -DskipTests package
RUN JAR_FILE=$(find target -maxdepth 1 -name '*.jar' ! -name '*original' | head -n 1) && cp "$JAR_FILE" /workspace/app.jar

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /workspace/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]