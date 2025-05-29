package si.um.feri.__Backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import si.um.feri.__Backend.service.FetchLogService;
import si.um.feri.__Backend.service.provider.ec_europa_euProvider;
import java.io.IOException;

@Component
public class ec_europa_euScheduler {
    private final ec_europa_euProvider listingService;
    private final FetchLogService fetchLogService;

    public ec_europa_euScheduler(ec_europa_euProvider service, FetchLogService fetchLogService) {
        this.listingService = service;
        this.fetchLogService = fetchLogService;
    }

//    @Scheduled(fixedDelayString = "${fetch.interval.forthcoming}")
//    public void fetchForthcomingListings() throws IOException {
//        System.out.println("Scheduled task: Fetching forthcoming listings.");
//        listingService.fetchListings("queryForthcoming.json");
//        fetchLogService.logFetch("ec_europa_eu", "Forthcoming");
//
//    }
//    @Scheduled(fixedDelayString = "${fetch.interval.open}")
//    public void fetchOpenListings() throws IOException {
//        System.out.println("Scheduled task: Fetching open listings.");
//        listingService.fetchListings("queryOpen.json");
//        fetchLogService.logFetch("ec_europa_eu", "Open");
//    }
//    @Scheduled(fixedDelayString = "${fetch.interval.closed}")
//    public void fetchClosedListings() throws IOException  {
//        System.out.println("Scheduled task: Fetching closed listings.");
//        listingService.fetchListings("queryClosed.json");
//        fetchLogService.logFetch("ec_europa_eu", "Closed");
//    }
}
