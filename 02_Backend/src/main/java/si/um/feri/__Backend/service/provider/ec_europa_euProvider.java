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
        Resource sortFile = new ClassPathResource("filters/ec_europa_euFilters/sortQuery.json");

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
            ec_europa_euRaw raw = mapper.treeToValue(itemNode, ec_europa_euRaw.class);
            Listing listing = new Listing();
            listing.setReference(raw.getReference());
            //  listing.setSummary(raw.getSummary());

            ec_europa_euRaw.Metadata m = raw.getMetadata();
            if (m != null) {
                listing.setSource("ec.europa.eu");
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

                listing.setDeadlineModel(first(m.getDeadlineModel()));
                listing.setProgrammePeriod(first(m.getProgrammePeriod()));
                listing.setBudgetOverview(cleanString(first(m.getBudgetOverview())));
                listing.setSupportInfo(cleanString(first(m.getSupportInfo())));
                listing.setSepTemplate(cleanString(first(m.getSepTemplate())));
                listing.setDescriptionByte(cleanString(first(m.getDescriptionByte())));
                listing.setLatestInfo(cleanString(first(m.getLatestInfos())));
                listing.setAction(cleanString(first(m.getActions())));
                listing.setKeywords(m.getKeywords());
                listing.setLinks(cleanString(first(m.getLinks())));

                if(m.getStatus() != null) {
                    if (Objects.equals(first(m.getStatus()), "31094503")) {
                        listing.setStatus("Closed");
                    } else if (Objects.equals(first(m.getStatus()), "31094501")) {
                        listing.setStatus("Forthcoming");
                    } else if (Objects.equals(first(m.getStatus()), "31094502")) {
                        listing.setStatus("Open");
                    }
                }
            }
            listings.add(listing);
        }
        listingRepository.saveAll(listings);
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
