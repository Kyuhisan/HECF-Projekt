package si.um.feri.__Backend.service;

import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import si.um.feri.__Backend.model.Listing;
import si.um.feri.__Backend.repository.ListingRepository;

import java.util.*;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    //-------------------------//
    // ec.europa.eu API CALLS  //
    //-------------------------//
    //  type: 0 - TENDERS, 1,2,8 - GRANTS, 6 - FUNDING UPDATES
    //  status: 31094502 - OPEN, 31094503 - CLOSED, 31094501 - FORTHCOMING
    //  frameworkProgramme: 43108390 - Horizon Europe

    public void fetchOpenListings() {
        String url = "https://api.tech.ec.europa.eu/search-api/prod/rest/search?apiKey=SEDIA&text=***&pageSize=100";

        String body = """
        {
            "bool": {
                "must": [
                    { "terms": { "type": ["0"] } },
                    { "terms": { "status": ["31094502"] } },
                    { "terms": { "frameworkProgramme": ["43108390"] } }
                ]
            },
            "sort": {
                "order": "DESC",
                "field": "startDate"
            }
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        System.out.println(response.getBody());

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");

        if (results != null) {
            List<Listing> listings = new ArrayList<>();
            for (Map<String, Object> r : results) {
                Listing f = new Listing();
                Map<String, Object> metadata = (Map<String, Object>) r.get("metadata");

                f.setReference((String) r.get("reference"));
                f.setUrl((String) r.get("url"));
                f.setSummary((String) r.get("summary"));
                f.setContent((String) r.get("content"));
                f.setLanguage((String) r.get("language"));

                if (metadata != null) {
                    f.setCallIdentifier((List<String>) metadata.get("callIdentifier"));
                    f.setKeywords((List<String>) metadata.get("keywords"));
                    f.setType((List<String>) metadata.get("type"));
                    f.setTypeMGA((List<String>) metadata.get("typeOfMGAs"));
                    f.setFrameworkProgramme((List<String>) metadata.get("frameworkProgramme"));
                    f.setIdentifier((List<String>) metadata.get("identifier"));
                    f.setProgrammePeriod((List<String>) metadata.get("programmePeriod"));
                    f.setDeadlineDate((List<String>) metadata.get("deadlineDate"));
                    f.setDeadlineModel((List<String>) metadata.get("deadlineModel"));
                    f.setTitle((List<String>) metadata.get("title"));
                }
                listings.add(f);
            }
            listingRepository.saveAll(listings);
        }
    }

    public void fetchForthcomingListings() {
        String url = "https://api.tech.ec.europa.eu/search-api/prod/rest/search?apiKey=SEDIA&text=***&pageSize=100";

        String body = """
        {
            "bool": {
                "must": [
                    {
                        "terms": {
                            "type": [
                                "0"
                            ]
                        }
                    },
                    {
                        "terms": {
                            "status": [
                                "31094501"
                            ]
                        }
                    },
                    {
                        "terms": {
                            "frameworkProgramme": [
                                "43108390"
                            ]
                        }
                    }
                ]
            }
            "sort": {
                "order": "DESC",
                "field": "startDate"
            }
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        System.out.println(response.getBody());

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");

        if (results != null) {
            List<Listing> listings = new ArrayList<>();
            for (Map<String, Object> r : results) {
                Listing f = new Listing();
                Map<String, Object> metadata = (Map<String, Object>) r.get("metadata");

                f.setReference((String) r.get("reference"));
                f.setUrl((String) r.get("url"));
                f.setSummary((String) r.get("summary"));
                f.setContent((String) r.get("content"));
                f.setLanguage((String) r.get("language"));

                if (metadata != null) {
                    f.setCallIdentifier((List<String>) metadata.get("callIdentifier"));
                    f.setKeywords((List<String>) metadata.get("keywords"));
                    f.setType((List<String>) metadata.get("type"));
                    f.setTypeMGA((List<String>) metadata.get("typeOfMGAs"));
                    f.setFrameworkProgramme((List<String>) metadata.get("frameworkProgramme"));
                    f.setIdentifier((List<String>) metadata.get("identifier"));
                    f.setProgrammePeriod((List<String>) metadata.get("programmePeriod"));
                    f.setDeadlineDate((List<String>) metadata.get("deadlineDate"));
                    f.setDeadlineModel((List<String>) metadata.get("deadlineModel"));
                    f.setTitle((List<String>) metadata.get("title"));
                }
                listings.add(f);
            }
            listingRepository.saveAll(listings);
        }
    }
    public void fetchClosedListings() {
        String url = "https://api.tech.ec.europa.eu/search-api/prod/rest/search?apiKey=SEDIA&text=***&pageSize=100";

        String body = """
        {
            "bool": {
                "must": [
                    {
                        "terms": {
                            "type": [
                                "0"
                            ]
                        }
                    },
                    {
                        "terms": {
                            "status": [
                                "31094503"
                            ]
                        }
                    },
                    {
                        "terms": {
                            "frameworkProgramme": [
                                "43108390"
                            ]
                        }
                    }
                ]
            }
            "sort": {
                "order": "DESC",
                "field": "startDate"
            }
        }
        """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        System.out.println(response.getBody());

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");

        if (results != null) {
            List<Listing> listings = new ArrayList<>();
            for (Map<String, Object> r : results) {
                Listing f = new Listing();
                Map<String, Object> metadata = (Map<String, Object>) r.get("metadata");

                f.setReference((String) r.get("reference"));
                f.setUrl((String) r.get("url"));
                f.setSummary((String) r.get("summary"));
                f.setContent((String) r.get("content"));
                f.setLanguage((String) r.get("language"));

                if (metadata != null) {
                    f.setCallIdentifier((List<String>) metadata.get("callIdentifier"));
                    f.setKeywords((List<String>) metadata.get("keywords"));
                    f.setType((List<String>) metadata.get("type"));
                    f.setTypeMGA((List<String>) metadata.get("typeOfMGAs"));
                    f.setFrameworkProgramme((List<String>) metadata.get("frameworkProgramme"));
                    f.setIdentifier((List<String>) metadata.get("identifier"));
                    f.setProgrammePeriod((List<String>) metadata.get("programmePeriod"));
                    f.setDeadlineDate((List<String>) metadata.get("deadlineDate"));
                    f.setDeadlineModel((List<String>) metadata.get("deadlineModel"));
                    f.setTitle((List<String>) metadata.get("title"));
                }
                listings.add(f);
            }
            listingRepository.saveAll(listings);
        }
    }
    public void fetchAllListings() {
        String url = "https://api.tech.ec.europa.eu/search-api/prod/rest/search?apiKey=SEDIA&text=***&pageSize=100";

        String body = """
        {
            "bool": {
                "must": [
                    {
                        "terms": {
                            "type": [
                                "0"
                            ]
                        }
                    },
                    {
                        "terms": {
                            "status": [
                                "31094501",
                                "31094502",
                                "31094503"
                            ]
                        }
                    },
                    {
                        "terms": {
                            "frameworkProgramme": [
                                "43108390"
                            ]
                        }
                    }
                ]
            }
            "sort": {
                "order": "DESC",
                "field": "startDate"
            }
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        System.out.println(response.getBody());

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");

        if (results != null) {
            List<Listing> listings = new ArrayList<>();
            for (Map<String, Object> r : results) {
                Listing f = new Listing();
                Map<String, Object> metadata = (Map<String, Object>) r.get("metadata");

                f.setReference((String) r.get("reference"));
                f.setUrl((String) r.get("url"));
                f.setSummary((String) r.get("summary"));
                f.setContent((String) r.get("content"));
                f.setLanguage((String) r.get("language"));

                if (metadata != null) {
                    f.setCallIdentifier((List<String>) metadata.get("callIdentifier"));
                    f.setKeywords((List<String>) metadata.get("keywords"));
                    f.setType((List<String>) metadata.get("type"));
                    f.setTypeMGA((List<String>) metadata.get("typeOfMGAs"));
                    f.setFrameworkProgramme((List<String>) metadata.get("frameworkProgramme"));
                    f.setIdentifier((List<String>) metadata.get("identifier"));
                    f.setProgrammePeriod((List<String>) metadata.get("programmePeriod"));
                    f.setDeadlineDate((List<String>) metadata.get("deadlineDate"));
                    f.setDeadlineModel((List<String>) metadata.get("deadlineModel"));
                    f.setTitle((List<String>) metadata.get("title"));
                }
                listings.add(f);
            }
            listingRepository.saveAll(listings);
        }
    }
}
