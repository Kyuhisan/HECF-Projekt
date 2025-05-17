package si.um.feri.__Backend.controller;

import org.springframework.web.bind.annotation.*;
import si.um.feri.__Backend.model.Listing;
import si.um.feri.__Backend.service.ListingService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin(origins = "http://localhost:5173")
public class ListingController {
    private final ListingService listingService;
    public ListingController(ListingService service) {
        this.listingService = service;
    }

    //  LISTING DATA FROM MONGODB
    @GetMapping("/showAll")
    public List<Listing> getAll() {
        return listingService.getAllListings();
    }
    @GetMapping("/showOpen")
    public List<Listing> getOpen() {
        return listingService.getOpenListings();
    }
    @GetMapping("/showForthcoming")
    public List<Listing> getForthcoming() {
        return listingService.getForthcomingListings();
    }
    @GetMapping("/showClosed")
    public List<Listing> getClosed() {
        return listingService.getClosedListings();
    }

    //  FETCHING DATA FROM API TO MONGODB
    @GetMapping("/fetch/forthcoming")
    public String fetchForthcomingListings() throws IOException {
        listingService.fetchListings("queryForthcoming.json");
        return "Forthcoming listings fetched and saved to MongoDB.";
    }
    @GetMapping("/fetch/open")
    public String fetchOpenListings() throws IOException {
        listingService.fetchListings("queryOpen.json");
        return "Open listings fetched and saved to MongoDB.";
    }
    @GetMapping("/fetch/closed")
    public String fetchClosedListings() throws IOException  {
        listingService.fetchListings("queryClosed.json");
        return "Closed listings fetched and saved to MongoDB.";
    }
    @GetMapping("/fetch/all")
    public String fetchAllListings() throws IOException {
        listingService.fetchListings("queryAll.json");
        return "All listings fetched and saved to MongoDB.";
    }
}

