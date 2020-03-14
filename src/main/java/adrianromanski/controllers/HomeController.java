package adrianromanski.controllers;

import adrianromanski.models.LocationStats;
import adrianromanski.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats =coronaVirusDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPrevDay).sum();

        allStats.sort((allStats1, allStats2) -> allStats2.getDiffFromPrevDay() - allStats1.getDiffFromPrevDay());

        LocationStats topDiffCountry = allStats.get(0);

        model.addAttribute("locationsStats", allStats);
        model.addAttribute("totalReportedCases", totalCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("topDiffCountry", topDiffCountry);
        return "home";
    }
}
