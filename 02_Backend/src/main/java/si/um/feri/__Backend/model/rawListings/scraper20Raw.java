package si.um.feri.__Backend.model.rawListings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class scraper20Raw {

    private String source;
    private String reference;
    private String summary;
    private Scraper20Metadata metadata;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Scraper20Metadata {
        private String summary;
        private String id;
        private String url;
        private String deadlineDate;
        private String technology;
        private String status;
        // private String massiveText
        //private String guidelines

    }
}
