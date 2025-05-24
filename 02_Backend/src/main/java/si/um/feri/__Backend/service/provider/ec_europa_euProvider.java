package si.um.feri.__Backend.service.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
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
import org.bson.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ec_europa_euProvider {
    private final ListingRepository listingRepository;
    private final MongoTemplate mongoTemplate;

    public ec_europa_euProvider(ListingRepository listingRepository, MongoTemplate mongoTemplate) {
        this.listingRepository = listingRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void fetchListings(String queryFileName) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        Resource queryFile = new ClassPathResource("filters/ec_europa_euFilters/" + queryFileName);
        Resource sortFile = new ClassPathResource("filters/ec_europa_euFilters/sortQuery.json");

        ObjectMapper mapper = new ObjectMapper();
        int pageSize = 50;
        int page = 1;
        List<String> allPages = new ArrayList<>();

        while (true) {
            String url = "https://api.tech.ec.europa.eu/search-api/prod/rest/search?apiKey=SEDIA&text=***&isExactMatch=true&pageSize=" + pageSize + "&pageNumber=" + page;
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpHeaders queryHeaders = new HttpHeaders();
            queryHeaders.setContentType(MediaType.APPLICATION_JSON);
            body.add("query", new HttpEntity<>(queryFile, queryHeaders));
            body.add("sort", new HttpEntity<>(sortFile, queryHeaders));

            HttpHeaders langHeaders = new HttpHeaders();
            langHeaders.setContentType(MediaType.APPLICATION_JSON);
            body.add("languages", new HttpEntity<>("[\"en\"]", langHeaders));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            //  SAVE FILTERED DATA TO MONGO
            String jsonBody = response.getBody();
            saveFilteredToMongo(jsonBody);
            allPages.add(jsonBody);

            JsonNode root = mapper.readTree(jsonBody);
            JsonNode results = root.path("results");

            if (!results.isArray() || results.isEmpty()) {
                break;
            }
            page++;
        }

        //  SET UP OUTPUT PATH
        String suffix = queryFileName
                .replace("query", "ec_europa_eu")
                .replace(".json", "RawData.json");

        //  SAVE RAW DATA LOCALLY
        String projectRoot = System.getProperty("user.dir");
        String outputPath = projectRoot + "/output/rawData/ec_europa_eu/" + suffix;
        saveRawLocally(allPages, outputPath);

        //  SAVE RAW DATA TO MONGODB
        String sourceStatus = "ec.europa.eu:" + queryFileName.replace("query", "").replace(".json", "");
        saveRawToMongo(allPages, sourceStatus);

        System.out.println("Fetched listings from ec.europa.eu");
    }

    public void saveRawLocally(List<String> jsonPages, String filePath) throws IOException {
        Files.createDirectories(Paths.get(filePath).getParent());
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("[\n");
            for (int i = 0; i < jsonPages.size(); i++) {
                writer.write(jsonPages.get(i));
                if (i < jsonPages.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
        }
        System.out.println("Saved raw data to: " + filePath);
    }

    public void saveRawToMongo(List<String> jsonPages, String provider) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        mongoTemplate.getCollection("Listings-Raw(ec.europa.eu)").deleteMany(new Document("sourceProvider", provider));

        for (String pageJson : jsonPages) {
            JsonNode root = mapper.readTree(pageJson);
            JsonNode results = root.path("results");

            if (!results.isArray()) continue;

            for (JsonNode resultNode : results) {
                Document bsonDoc = Document.parse(resultNode.toString());
                bsonDoc.put("sourceProvider", provider);

                mongoTemplate.insert(bsonDoc, "Listings-Raw(ec.europa.eu)");
            }
        }
        System.out.println("Replaced raw listings in MongoDB for provider: " + provider);
    }

    private void saveFilteredToMongo(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode itemsNode = root.path("results");

        if (!itemsNode.isArray()) return;

        List<Listing> listingsToSave = new ArrayList<>();

        for (JsonNode itemNode : itemsNode) {
            ec_europa_euRaw raw = mapper.treeToValue(itemNode, ec_europa_euRaw.class);
            Listing listing = new Listing();
            ec_europa_euRaw.Metadata m = raw.getMetadata();

            //  SOURCE UNIQUE IDENTIFIERS
            String identifier = first(m.getIdentifier());
            listing.setSourceIdentifier(identifier);

            //  STATUS
            switch (first(m.getStatus())) {
                case "31094501" -> listing.setStatus("Forthcoming");
                case "31094502" -> listing.setStatus("Open");
                case "31094503" -> listing.setStatus("Closed");
                default -> {
                    listing.setStatus("Unknown");
                }
            }

            //  CHECK DUPLICATES
            String sourceIdentifier = "ec.europa.eu:" + identifier + ":" + listing.getStatus();
            if (listingRepository.existsBySourceIdentifier(sourceIdentifier)) {
                System.out.println("Duplicate listing found: " + sourceIdentifier);
                continue;
            }
            listing.setSourceIdentifier(sourceIdentifier);

            //  CHECK DEADLINE MODEL
            if (!Objects.equals(first(m.getDeadlineModel()), "single-stage")) {
                System.out.println("Deadline model is not single-stage!");
                continue;
            }

            //  SOURCE
            listing.setSource("ec.europa.eu");

            //  TITLE
            listing.setTitle(first(m.getTitle()));

            //  START/END DATES
            listing.setStartDate(convertDate(first(m.getStartDate())));
            listing.setDeadlineDate(convertDate(first(m.getDeadlineDate())));

            //  URL
            listing.setUrl("https://ec.europa.eu/info/funding-tenders/opportunities/portal/screen/opportunities/topic-details/" + identifier);

            //  SUMMARY
            listing.setSummary(cleanString(first(m.getDescriptionByte())));
            listing.setDescription(cleanString(first(m.getDescriptionByte())));

            //  KEYWORDS
            listing.setKeywords(m.getTags());

            //  INDUSTRIES
            listing.setIndustries(m.getCrossCuttingPriorities());

            //  TECHNOLOGIES
            listing.setTechnologies(m.getTypesOfAction());

            //  BUDGET
            String budget = first(m.getBudget());

            if (budget != null && !budget.isBlank()) {
                listing.setBudget(budget);
            } else if (m.getBudgetOverview() != null && !m.getBudgetOverview().isEmpty() && identifier != null) {
                String overviewJson = m.getBudgetOverview().get(0);
                JsonNode overviewNode = new ObjectMapper().readTree(overviewJson);
                JsonNode topicMap = overviewNode.path("budgetTopicActionMap");

                double matchedBudget = 0;
                boolean foundMatch = false;

                if (topicMap.isObject()) {
                    for (JsonNode actionsArray : topicMap) {
                        for (JsonNode action : actionsArray) {
                            String actionName = action.path("action").asText();

                            if (actionName != null && (actionName.startsWith(identifier) || actionName.contains(identifier))) {
                                foundMatch = true;
                                JsonNode yearMap = action.path("budgetYearMap");

                                if (yearMap.isObject()) {
                                    for (JsonNode amountNode : yearMap) {
                                        matchedBudget += amountNode.asDouble();
                                    }
                                }
                            }
                        }
                    }
                }
                if (foundMatch && matchedBudget > 0) {
                    listing.setBudget(String.format("%.0f", matchedBudget));
                }
            }
            listingsToSave.add(listing);

        }
        listingRepository.saveAll(listingsToSave);
    }

    //  GET FIRST ELEMENT OF LIST
    private String first(List<String> list) {
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    //  CONVERT DATE FROM API TO DD/MM/YYYY
    public static String convertDate(String inputDate) {
        OffsetDateTime odt = OffsetDateTime.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        return odt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    //  CLEAN STRING OF HTML TAGS
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
