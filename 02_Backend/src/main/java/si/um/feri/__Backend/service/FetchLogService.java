package si.um.feri.__Backend.service;

import org.springframework.stereotype.Service;
import si.um.feri.__Backend.model.FetchLog;
import si.um.feri.__Backend.repository.FetchLogRepository;

import java.time.LocalDateTime;

@Service
public class FetchLogService {

    private final FetchLogRepository repository;

    public FetchLogService(FetchLogRepository repository) {
        this.repository = repository;
    }

    public void logFetch(String source, String type) {
        FetchLog log = new FetchLog(source, type, LocalDateTime.now());
        repository.save(log);
    }
}