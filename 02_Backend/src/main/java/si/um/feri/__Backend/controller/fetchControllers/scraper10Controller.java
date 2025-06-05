package si.um.feri.__Backend.controller.fetchControllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.um.feri.__Backend.service.provider.scraper10Provider;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/listings/scraper10")
@CrossOrigin(origins = "${spring.mvc.cors.allowed-origins}")
public class scraper10Controller {
//  private final scraper10Provider scraper10Provider;
//  public scraper10Controller(scraper10Provider scraper10) {
//    this.scraper10Provider = scraper10;
//  }
//
//  @GetMapping("/scrape/all")
//  public List<Map<String, Object>> getInfo10() throws IOException {
//      return scraper10Provider.scrapeData();
//  }
}
