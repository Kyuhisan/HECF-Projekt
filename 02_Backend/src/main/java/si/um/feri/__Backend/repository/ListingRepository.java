package si.um.feri.__Backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.__Backend.model.Listing;

public interface ListingRepository extends MongoRepository<Listing, String> {
    boolean existsBySourceIdentifier(String sourceIdentifier);
}
