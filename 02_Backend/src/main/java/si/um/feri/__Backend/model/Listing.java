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
    private String summary;
    private String url;
//    private String latestInfo;
//    private String sepTemplate;
//    private List<String> keywords;
    private String title;
    private String callIdentifier;
//      private String links;
    private String callTitle;
//    private String descriptionByte;
    private String identifier;
//    private String programmePeriod;
    private String deadlineDate;
//    private String action;
//    private String budgetOverview;
    private String startDate;
//    private String deadlineModel;
//    private String supportInfo;
    private String status;
}
