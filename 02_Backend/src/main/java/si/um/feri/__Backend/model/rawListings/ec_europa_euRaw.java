package si.um.feri.__Backend.model.rawListings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ec_europa_euRaw {
    private String source;
    private String summary;
    private Metadata metadata;

    //  private String reference;
    //  private String apiVersion;
    //  private String url;
    //  private String title;
    //  private String contentType;
    //  private String language;
    //  private String databaseLabel;
    //  private String database;
    //  private String weight;
    //  private String groupById;
    //  private String content;
    //  private String accessRestriction;
    //  private String pages;
    //  private String checksum;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        private List<String> identifier;
        private List<String> title;
        private List<String> deadlineDate;
        private List<String> status;
        private List<String> url;

//      private List<String> latestInfos;
//      private List<String> sepTemplate;
//      private List<String> keywords;
//      private List<String> topicConditions;
//      private List<String> type;
//      private List<String> typeOfMGAs;
//      private List<String> callIdentifier;
//      private List<String> links;
//      private List<String> callTitle;
//      private List<String> frameworkProgramme;
//      private List<String> descriptionByte;
//      private List<String> programmePeriod;
//      private List<String> actions;
//      private List<String> budgetOverview;
//      private List<String> startDate;
//      private List<String> deadlineModel;
//      private List<String> supportInfo;
//      private List<String> es_SortDate;
//      private List<String> es_SortStatus;
//      private List<String> language;
//      private List<String> esST_checksum;
//      private List<String> focusArea;
//      private List<String> esST_Filename;
//      private List<String> ccm2Id;
//      private List<String> callccm2Id;
//      private List<String> DATASOURCE;
//      private List<String> REFERENCE;
//      private List<String> es_ContentType;
//      private List<String> programmeDivision;
//      private List<String> crossCuttingPriorities;
//      private List<String> esDA_IngestDate;
//      private List<String> typesOfAction;
//      private List<String> es_Combine;
//      private List<String> esST_URL;
//      private List<String> tags;
//      private List<String> cenTagsA;
//      private List<String> esDA_QueueDate;
//      private List<String> allowPartnerSearch;
//      private List<String> specificObjective;
    }
}

