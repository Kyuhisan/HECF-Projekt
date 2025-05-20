package si.um.feri.__Backend.model.rawListings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class scraper10Raw {
    private String source;
    private String reference;
    private String summary;
    private Scraper10Metadata metadata;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Scraper10Metadata {
        private String summary;
        private String id;
        private String url;
        private String deadlineDate;
        private String technology;
        private String domains;
        private String typeOfBeneficiary;
        private String maxFunding;


    }
}




