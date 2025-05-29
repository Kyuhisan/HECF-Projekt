package si.um.feri.__Backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import si.um.feri.__Backend.service.FetchLogService;
import si.um.feri.__Backend.service.provider.scraper10Provider;
import java.io.IOException;

@Component
public class scraper10Scheduler {
    private final scraper10Provider scraper10Provider;
    private final FetchLogService fetchLogService;

    public scraper10Scheduler(scraper10Provider scraper10, FetchLogService fetchLogService) {
      this.scraper10Provider = scraper10;
        this.fetchLogService = fetchLogService;
    }

//    @Scheduled(fixedDelayString = "${fetch.interval.open}")
//    public void getInfo10() throws IOException {
//        System.out.println("Scheduled task: Fetching data from scraper10.");
//        scraper10Provider.scrapeData();
//        fetchLogService.logFetch("scraper10", "Open");
//    }
}
