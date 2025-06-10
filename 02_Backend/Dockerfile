FROM amazoncorretto:23

WORKDIR /app
COPY target/*.jar app.jar

# Copy the entire output directory structure
COPY output/keywords /app/output/keywords

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080