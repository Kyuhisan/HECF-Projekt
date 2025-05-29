package si.um.feri.__Backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import si.um.feri.__Backend.service.FetchLogService;
import si.um.feri.__Backend.service.provider.scraper20Provider;
import java.io.IOException;

@Component
public class scraper20Scheduler {
    private  final scraper20Provider scraper20Provider;
    private final FetchLogService fetchLogService;

    public scraper20Scheduler(scraper20Provider scraper20, FetchLogService fetchLogService) {
        this.scraper20Provider = scraper20;
        this.fetchLogService = fetchLogService;
    }

//    @Scheduled(fixedDelayString = "${fetch.interval.open}")
//    public void getInfo20() throws IOException {
//        System.out.println("Scheduled task: Fetching data from scraper20.");
//        scraper20Provider.scrapeData();
//        fetchLogService.logFetch("scraper20", "All");
//    }
}
