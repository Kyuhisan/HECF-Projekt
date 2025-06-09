package si.um.feri.__Backend.controller.fetchControllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import si.um.feri.__Backend.service.provider.scraper20Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/listings/scraper20")
@CrossOrigin
public class scraper20Controller {
//    private  final scraper20Provider scraper20Provider;
//    public scraper20Controller(scraper20Provider scraper20) {
//        this.scraper20Provider = scraper20;
//    }
//
//    @GetMapping("/scrape/all")
//    public List<Map<String, Object>> getInfo20() throws IOException {
//        return scraper20Provider.scrapeData();
//    }
}
