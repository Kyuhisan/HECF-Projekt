package si.um.feri.__Backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "listingsTesting")
@Data
public class Listing {
    @Id
    private String id;

    @Indexed(unique = true)
    private String sourceIdentifier;

    private String status;
    private String source;
    private String url;
    private String title;
    private String summary;
    private String startDate;
    private String deadlineDate;
    private String budget;
}
