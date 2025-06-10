FROM amazoncorretto:23

WORKDIR /app
COPY target/*.jar app.jar
COPY output/keywords/ec_europa_eu/*.txt output/keywords/ec_europa_eu/keywords.txt/
COPY output/keywords/generic/*.txt output/keywords/generic/keywords.txt
COPY output/keywords/scraper10/*.txt output/keywords/scraper10/keywords.txt
COPY output/keywords/scraper20/*.txt output/keywords/scraper20/keywords.txt

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080