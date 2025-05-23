package si.um.feri.__Backend.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import si.um.feri.__Backend.service.OpenAiService;

@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = "http://localhost:5173")
public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/keywords")
    public Mono<String> extractKeywords(@RequestBody String userInput) {
        return openAiService.extractKeywords(userInput);
    }
}
