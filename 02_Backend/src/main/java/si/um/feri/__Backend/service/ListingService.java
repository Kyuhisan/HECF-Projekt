package si.um.feri.__Backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import si.um.feri.__Backend.model.Listing;
import si.um.feri.__Backend.model.RawListing;
import si.um.feri.__Backend.repository.ListingRepository;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListingService {
    private final ListingRepository listingRepository;
    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    //  DATA DISPLAY FROM MONGODB
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }
    public List<Listing> getForthcomingListings() {
        return listingRepository.findAll().stream()
                .filter(l -> "31094501".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }
    public List<Listing> getOpenListings() {
        return listingRepository.findAll().stream()
                .filter(l -> "31094502".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }
    public List<Listing> getClosedListings() {
        return listingRepository.findAll().stream()
                .filter(l -> "31094503".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }

    //  DATA FETCHING FROM API
    public void fetchListings(String queryFileName) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        Resource queryFile = new ClassPathResource("filters/" + queryFileName);
        Resource sortFile = new ClassPathResource("filters/sortQuery.json");

        ObjectMapper mapper = new ObjectMapper();
        int pageSize = 25;
        int totalPages = 1;

        for (int page = 1; page <= totalPages; page++) {
            String url = "https://api.tech.ec.europa.eu/search-api/prod/rest/search" +
                    "?apiKey=SEDIA&text=***&pageSize=" + pageSize + "&pageNumber=" + page;

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("query", queryFile);
            body.add("sort", sortFile);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            String jsonBody = response.getBody();
            saveToMongo(jsonBody);

            if (page == 1) {
                JsonNode root = mapper.readTree(jsonBody);
                int totalResults = root.path("totalResults").asInt();
                totalPages = (int) Math.ceil((double) totalResults / pageSize);
            }
        }
    }
    private void saveToMongo(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode itemsNode = root.path("results");

        if (!itemsNode.isArray()) return;
        List<Listing> listings = new ArrayList<>();

        for (JsonNode itemNode : itemsNode) {
            RawListing raw = mapper.treeToValue(itemNode, RawListing.class);
            Listing listing = new Listing();
            listing.setReference(raw.getReference());
            listing.setSummary(raw.getSummary());

            RawListing.Metadata m = raw.getMetadata();
            if (m != null) {
                listing.setTitle(first(m.getTitle()));
                listing.setCallIdentifier(first(m.getCallIdentifier()));
                listing.setCallTitle(first(m.getCallTitle()));
                listing.setStartDate(first(m.getStartDate()));
                listing.setDeadlineDate(first(m.getDeadlineDate()));

                String identifier = first(m.getIdentifier());
                listing.setIdentifier(identifier);

                if (identifier != null && !identifier.isBlank()) {
                    listing.setUrl("https://ec.europa.eu/info/funding-tenders/opportunities/portal/screen/opportunities/topic-details/" + identifier);
                }

//                listing.setDeadlineModel(first(m.getDeadlineModel()));
//                listing.setProgrammePeriod(first(m.getProgrammePeriod()));
//                listing.setBudgetOverview(first(m.getBudgetOverview()));
//                listing.setSupportInfo(first(m.getSupportInfo()));
//                listing.setSepTemplate(first(m.getSepTemplate()));
//                listing.setDescriptionByte(first(m.getDescriptionByte()));
//                listing.setLatestInfo(first(m.getLatestInfos()));
//                listing.setAction(first(m.getActions()));
//                listing.setKeywords(m.getKeywords());
//                listing.setLinks(first(m.getLinks()));
                listing.setStatus(first(m.getStatus()));
            }
            listings.add(listing);
        }
        listingRepository.saveAll(listings);
    }
    private String first(List<String> list) {
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }
}