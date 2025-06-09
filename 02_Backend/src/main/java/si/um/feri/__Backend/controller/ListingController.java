package si.um.feri.__Backend.controller;

import org.springframework.web.bind.annotation.*;
import si.um.feri.__Backend.model.Listing;
import si.um.feri.__Backend.service.ListingService;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
@CrossOrigin
public class ListingController {
    private final ListingService listingService;
    public ListingController(ListingService service) {
        this.listingService = service;
    }

    @GetMapping("/show/all")
    public List<Listing> getAll() {
        return listingService.getAllListings();
    }
    @GetMapping("/show/open")
    public List<Listing> getOpen() {
        return listingService.getOpenListings();
    }
    @GetMapping("/show/forthcoming")
    public List<Listing> getForthcoming() {
        return listingService.getForthcomingListings();
    }
    @GetMapping("/show/closed")
    public List<Listing> getClosed() {
        return listingService.getClosedListings();
    }
}

