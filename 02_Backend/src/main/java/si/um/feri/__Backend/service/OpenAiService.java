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

        return keywords.stream()
                .map(k -> "\"" + k + "\"")
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public Mono<String> extractKeywords(String userInput) {
        String keywordList;
        try {
            String baseDir = System.getProperty("user.dir") + "/output/keywords/";
            keywordList = loadAllKeywordsFromFiles(List.of(
                    baseDir + "generic/keywords.txt",
                    baseDir + "scraper10/keywords.txt",
                    baseDir + "scraper20/keywords.txt",
                    baseDir + "ec_europa_eu/keywords.txt"
            ));
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Failed to load keywords from files", e));
        }

        String prompt = """
You are an assistant that classifies user input into high-level domains.
From the list of keywords below,  select **exactly between 5 and 15** that are semantically or thematically relevant to the user input.
Choose keywords that are directly or indirectly related to the concepts mentioned.

Only return a JSON array of the selected keywords (as strings). Do not include any explanation or additional text.

Important:
- Select at least 5 and no more than 15 keywords. Never return more than 20.
- Avoid exact duplicates.
- You may invent up to 2 additional keywords in english, if they are clearly relevant and missing from the list.
- from every txt file  choose 3 to 4 keywords.

Only choose from the following keywords:
""" + keywordList + "\n\nUser input: \"" + userInput + "\"";

        Map<String, Object> requestBody = Map.of(
                "model", "mistralai/mistral-7b-instruct:free",  // FREE model
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.3
        );

        return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("HTTP-Referer", "http://localhost:5173") // lahko poljubno
                .header("X-Title", "HECF-SmartSearch") // ime tvoje aplikacije kaj je v titlu
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