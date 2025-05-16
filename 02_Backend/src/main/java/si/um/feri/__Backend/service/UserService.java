package si.um.feri.__Backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.um.feri.__Backend.model.User;
import si.um.feri.__Backend.repository.UserRepository;
import java.util.List;
import java.util.Arrays;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void fetchAndSaveUsers() {
        String url = "https://jsonplaceholder.typicode.com/users";
        User[] users = restTemplate.getForObject(url, User[].class);
        if (users != null) {
            userRepository.saveAll(Arrays.asList(users));
        }
    }
}

