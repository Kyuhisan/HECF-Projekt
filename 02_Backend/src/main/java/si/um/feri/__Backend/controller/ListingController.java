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

    @GetMapping("/all")
    public List<Listing> getAll() {
        return listingService.getAllListings();
    }

    @GetMapping("/fetchOpenListings")
    public String fetchOpenListings() throws IOException {
        listingService.fetchOpenListings();
        return "Open listings fetched and saved to MongoDB.";
    }

    @GetMapping("/fetchForthcomingListings")
    public String fetchForthcomingListings() {
        listingService.fetchForthcomingListings();
        return "Forthcoming listings fetched and saved to MongoDB.";
    }

    @GetMapping("/fetchClosedListings")
    public String fetchClosedListings() {
        listingService.fetchClosedListings();
        return "Closed listings fetched and saved to MongoDB.";
    }

    @GetMapping("/fetchAllListings")
    public String fetchAllListings() {
        listingService.fetchAllListings();
        return "All listings fetched and saved to MongoDB.";
    }
}

