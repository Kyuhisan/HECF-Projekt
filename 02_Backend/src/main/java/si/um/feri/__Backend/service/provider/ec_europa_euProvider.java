package si.um.feri.__Backend.service.provider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import si.um.feri.__Backend.model.Listing;
import si.um.feri.__Backend.model.rawListings.ec_europa_euRaw;
import si.um.feri.__Backend.repository.ListingRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ec_europa_euProvider {
    private final ListingRepository listingRepository;
    public ec_europa_euProvider(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public void fetchListings(String queryFileName) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        Resource queryFile = new ClassPathResource("filters/ec_europa_euFilters/" + queryFileName);
        ObjectMapper mapper = new ObjectMapper();
        int pageSize = 50;
        int page = 1;

        while (true) {
            String url = "https://api.tech.ec.europa.eu/search-api/prod/rest/search?apiKey=SEDIA&text=***&isExactMatch=true&pageSize=" + pageSize + "&pageNumber=" + page;

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            //  File filters
            HttpHeaders queryHeaders = new HttpHeaders();
            queryHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Resource> queryEntity = new HttpEntity<>(queryFile, queryHeaders);
            body.add("query", queryEntity);

            //  Languages filter
            HttpHeaders langHeaders = new HttpHeaders();
            langHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> langEntity = new HttpEntity<>("[\"en\"]", langHeaders);
            body.add("languages", langEntity);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            String jsonBody = response.getBody();
            saveToMongo(jsonBody);

            JsonNode root = mapper.readTree(jsonBody);
            JsonNode results = root.path("results");

            if (!results.isArray() || results.isEmpty()) {
                break;
            }
            page++;
        }

        System.out.println("Fetched listings from ec.europa.eu");
    }

    private void saveToMongo(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode itemsNode = root.path("results");

        if (!itemsNode.isArray()) return;

        List<Listing> listingsToSave = new ArrayList<>();
        int totalCount = 0;
        int savedCount = 0;
        int skippedNoId = 0;
        int skippedDuplicate = 0;

        int statusForthcoming = 0;
        int statusOpen = 0;
        int statusClosed = 0;
        int statusUnknown = 0;

        for (JsonNode itemNode : itemsNode) {
            totalCount++;

            ec_europa_euRaw raw = mapper.treeToValue(itemNode, ec_europa_euRaw.class);
            Listing listing = new Listing();

            listing.setId(raw.getReference());

            ec_europa_euRaw.Metadata m = raw.getMetadata();
            if (m == null) {
                System.out.println("Skipping entry with null metadata.");
                continue;
            }

            listing.setSource("ec.europa.eu");
            listing.setTitle(first(m.getTitle()));
            listing.setDeadlineDate(first(m.getDeadlineDate()));
            String identifier = first(m.getIdentifier());

            if (raw.getReference() == null || raw.getReference().isBlank()) {
                System.out.println("Skipping listing with missing identifier.");
                skippedNoId++;
                continue;
            }

            listing.setUrl("https://ec.europa.eu/info/funding-tenders/opportunities/portal/screen/opportunities/topic-details/" + identifier);

            if (listingRepository.existsById(raw.getReference())) {
                System.out.println("Skipping duplicate listing with ID: " + raw.getReference());
                skippedDuplicate++;
                continue;
            }

            String rawStatus = first(m.getStatus());
            if (rawStatus == null) {
                listing.setStatus("Unknown");
                statusUnknown++;
            } else if (rawStatus.equals("31094501")) {
                listing.setStatus("Forthcoming");
                statusForthcoming++;
            } else if (rawStatus.equals("31094502")) {
                listing.setStatus("Open");
                statusOpen++;
            } else if (rawStatus.equals("31094503")) {
                listing.setStatus("Closed");
                statusClosed++;
            } else {
                listing.setStatus("Unknown");
                statusUnknown++;
                System.out.println("Unmapped status code: " + rawStatus + " for ID: " + identifier);
            }
            listingsToSave.add(listing);
            savedCount++;
        }
        listingRepository.saveAll(listingsToSave);

        System.out.println("Total listings parsed: " + totalCount);
        System.out.println("Saved listings: " + savedCount);
        System.out.println("Skipped (no ID): " + skippedNoId);
        System.out.println("Skipped (duplicate): " + skippedDuplicate);
        System.out.println("Status counts: Forthcoming=" + statusForthcoming + ", Open=" + statusOpen + ", Closed=" + statusClosed + ", Unknown=" + statusUnknown);
    }

    private String first(List<String> list) {
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    private String cleanString(String rawString) {
        if (rawString == null || rawString.isEmpty()) {
            return "";
        }

        rawString = rawString.replaceAll("(?is)<script.*?>.*?</script>", "");
        rawString = rawString.replaceAll("(?is)<style.*?>.*?</style>", "");
        rawString = rawString.replaceAll("<[^>]+>", "");
        rawString = rawString.replaceAll("&nbsp;", " ");
        rawString = rawString.replaceAll("&amp;", "&");
        rawString = rawString.replaceAll("\\s+", " ").trim();
        return rawString;
    }
}
