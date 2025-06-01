package si.um.feri.__Backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final WebClient webClient = WebClient.builder().build();

    private String loadAllKeywordsFromFiles(List<String> filePaths) throws IOException {
        Set<String> keywords = new LinkedHashSet<>();

        for (String path : filePaths) {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                String cleaned = line.trim();
                if (!cleaned.isEmpty()) {
                    keywords.add(cleaned);
                }
            }
        }

        // Limit to first 300 keywords to avoid token overload
        return keywords.stream()
                .map(k -> "- " + k)
                .collect(Collectors.joining("\n"));
    }

    public Mono<String> extractKeywords(String userInput) {
        String formattedKeywordList;
        try {
            String baseDir = System.getProperty("user.dir") + "/output/keywords/";
            formattedKeywordList = loadAllKeywordsFromFiles(List.of(
                    baseDir + "scraper10/keywords.txt",
                    baseDir + "scraper20/keywords.txt",
                    baseDir + "generic/keywords.txt",
                    baseDir + "ec_europa_eu/keywords.txt"
            ));
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Failed to load keywords from files", e));
        }

        String prompt = """
You are a keyword extractor.

Given a user input and a list of available keywords, select ONLY between 5 and 10 relevant keywords based on thematic or semantic similarity.

Rules:
- You may invent up to 2 keywords if necessary.
- No duplicates.
- All keywords must be in english language!
- Return only a JSON array of strings.

Format:
["keyword1", "keyword2", ..., "keywordN"]

Available Keywords:
""" + formattedKeywordList + "\n\nUser Input:\n" + userInput;

        Map<String, Object> requestBody = Map.of(
                "model", "mistralai/mistral-7b-instruct:free",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant for keyword classification."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.3
        );

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("HTTP-Referer", "http://localhost:5173")
                .header("X-Title", "HECF-SmartSearch")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    System.out.println("Raw response: " + response); // Debugging output

                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return message.get("content").toString().trim();
                    }
                    return "[]";
                })
                .onErrorResume(e -> {
                    System.err.println("OpenRouter call failed: " + e.getMessage());
                    return Mono.just("[]");
                });
    }
}