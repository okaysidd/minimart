FROM eclipse-temurin:21-jre
WORKDIR /app

COPY otel/otel.jar /otel.jar
COPY target/order-svc-*.jar app.jar

ENTRYPOINT ["java", "-javaagent:/otel.jar", "-jar", "app.jar"]
