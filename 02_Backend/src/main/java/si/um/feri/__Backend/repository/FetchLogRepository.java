package si.um.feri.__Backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.__Backend.model.FetchLog;

public interface FetchLogRepository extends MongoRepository<FetchLog, String> {}
