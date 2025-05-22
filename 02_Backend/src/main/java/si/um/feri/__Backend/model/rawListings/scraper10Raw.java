package si.um.feri.__Backend.model.rawListings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class scraper10Raw {
    private String id;
    private String status;
    private String source;
    private String url;
    private String title;
    private String summary;
    // NEEDS STARTDATE
    private String deadlineDate;
    private String maxFunding;

//    private String reference;
//    private String technology;
//    private String domains;
//    private String typeOfBeneficiary;
}