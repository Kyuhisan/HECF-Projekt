package si.um.feri.__Backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "listings")
@Data
public class Listing {
    @Id
    private String reference;
    private String url;
    private String summary;
    private String content;
    private String language;
    private List<String> callIdentifier;
    private List<String> keywords;
    private List<String> type;
    private List<String> typeMGA;
    private List<String> frameworkProgramme;
    private List<String> identifier;
    private List<String> programmePeriod;
    private List<String> deadlineDate;
    private List<String> startDate;
    private List<String> status;
    private List<String> deadlineModel;
    private List<String> title;
}
