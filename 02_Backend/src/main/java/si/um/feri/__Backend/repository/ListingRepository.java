package si.um.feri.__Backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.__Backend.model.Listing;

import java.util.List;
import java.util.Set;

public interface ListingRepository extends MongoRepository<Listing, String> {
    boolean existsBySourceIdentifier(String sourceIdentifier);
    List<Listing> findAllBySourceIdentifierIn(List<String> identifiers);
    List<Listing> findAllBySourceIdentifierIn(Set<String> sourceIdentifiers);

}
