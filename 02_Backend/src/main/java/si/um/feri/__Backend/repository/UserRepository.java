package si.um.feri.__Backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.__Backend.model.User;

public interface UserRepository extends MongoRepository<User, String> {}
