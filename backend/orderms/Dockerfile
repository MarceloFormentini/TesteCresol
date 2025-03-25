FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY backend/orderms/pom.xml ./
RUN mvn dependency:go-offline

COPY backend/orderms/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /app/target/orderms-*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]