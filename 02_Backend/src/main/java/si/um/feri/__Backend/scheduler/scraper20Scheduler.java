package si.um.feri.__Backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import si.um.feri.__Backend.service.provider.scraper20Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class scraper20Scheduler {
    private  final scraper20Provider scraper20Provider;
    public scraper20Scheduler(scraper20Provider scraper20) {
        this.scraper20Provider = scraper20;
    }

    @Scheduled(fixedDelayString = "${fetch.interval.open}")
    public List<Map<String, Object>> getInfo20() throws IOException {
        System.out.println("Scheduled task: Fetching data from scraper20.");
        return scraper20Provider.scrapeData();
    }
}
