package si.um.feri.__Backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder().build();

    public Mono<String> extractKeywords(String userInput) {
        String prompt = """
You are an assistant that classifies text into high-level domains.
From the following list of keywords, select exactly 5 that are most relevant to the user input.
Only return a JSON array of the selected keywords (as strings), no explanation.

Keywords:
Manufacturing, Mobility, Tourism, Energy, Healthcare, Natural Resources, Transportation, Food and Beverage,
Aerospace, Agriculture, Construction, Automotive, Media and Entertainment, Sustainability, Textiles,
Electronics, Renewable Energy, Education, Health care, Art, CleanTech, GreenTech, Artificial Intelligence,
Next Generation Internet, Cyber Security, Augmented Reality, Internet of Things, Virtual Reality,
Data Integration, Big Data, Robotics, Cloud Computing, 5G, Blockchain, Data Visualization,
Industrial Automation, Edge devices, Machine Learning, Drones, Biotechnology

User input:
""" + "\"" + userInput + "\"";
        Map<String, Object> requestBody = Map.of(
                "model", "mistralai/mistral-7b-instruct:free",  // FREE model
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.3
        );

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("HTTP-Referer", "http://localhost:5173") // lahko poljubno
                .header("X-Title", "HECF-SmartSearch") // ime tvoje aplikacije
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return message.get("content").toString();
                    }
                    return "[]";
                })
                .onErrorResume(e -> {
                    System.err.println("OpenRouter call failed: " + e.getMessage());
                    return Mono.just("[]");
                });
    }
}