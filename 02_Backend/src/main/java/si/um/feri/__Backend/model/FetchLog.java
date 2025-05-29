package si.um.feri.__Backend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "fetch_logs")
@Data
public class FetchLog {
    @Id
    private String id;
    private String source;
    private String status;
    private LocalDateTime timeOfFetch;

    public FetchLog(String source, String status, LocalDateTime timeOfFetch){
        this.source = source;
        this.status = status;
        this.timeOfFetch = timeOfFetch;
    }
}
