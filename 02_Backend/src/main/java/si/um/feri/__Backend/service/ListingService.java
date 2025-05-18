package si.um.feri.__Backend.service;

import org.springframework.stereotype.Service;
import si.um.feri.__Backend.model.Listing;
import si.um.feri.__Backend.repository.ListingRepository;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }
    public List<Listing> getForthcomingListings() {
        return listingRepository.findAll().stream()
                .filter(l -> "Forthcoming".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }
    public List<Listing> getOpenListings() {
        return listingRepository.findAll().stream()
                .filter(l -> "Open".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }
    public List<Listing> getClosedListings() {
        return listingRepository.findAll().stream()
                .filter(l -> "Closed".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }
}