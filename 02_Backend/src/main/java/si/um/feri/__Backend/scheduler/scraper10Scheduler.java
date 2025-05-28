package si.um.feri.__Backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import si.um.feri.__Backend.service.provider.scraper10Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class scraper10Scheduler {
    private final scraper10Provider scraper10Provider;
    public scraper10Scheduler(scraper10Provider scraper10) {
      this.scraper10Provider = scraper10;
    }

    @Scheduled(fixedDelayString = "${fetch.interval.open}")
    public List<Map<String, Object>> getInfo10() throws IOException {
        System.out.println("Scheduled task: Fetching data from scraper10.");
        return scraper10Provider.scrapeData();
    }
}
