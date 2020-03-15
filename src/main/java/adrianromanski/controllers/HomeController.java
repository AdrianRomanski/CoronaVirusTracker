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

    @GetMapping("/list")
    public String home(Model model) {
        List<LocationStats> allStats =coronaVirusDataService.getAllStats();
        allStats.sort(Comparator.comparing(LocationStats::getCountry));
        model.addAttribute("locationsStats", allStats);
        return "list";
    }

    @GetMapping("/home")
    public String barGraph(Model model) {
        List<LocationStats> allStats =coronaVirusDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPrevDay).sum();
        allStats.sort((allStats1, allStats2) -> allStats2.getDiffFromPrevDay() - allStats1.getDiffFromPrevDay());
        LocationStats topDiffCountry = allStats.get(0);

        allStats.sort((allStats1, allStats2) -> allStats2.getLatestTotalCases() - allStats1.getLatestTotalCases());
        LocationStats topCasesCountry = allStats.get(0);
        allStats.sort(Comparator.comparing(LocationStats::getCountry));

        Map<String, Integer> topDiff = new HashMap<>();
        topDiff.put(allStats.get(0).getCountry(), allStats.get(0).getLatestTotalCases());
        topDiff.put(allStats.get(1).getCountry(), allStats.get(1).getLatestTotalCases());
        topDiff.put(allStats.get(2).getCountry(), allStats.get(2).getLatestTotalCases());
        topDiff.put(allStats.get(3).getCountry(), allStats.get(3).getLatestTotalCases());

        model.addAttribute("topDiff", topDiff);
        model.addAttribute("locationsStats", allStats);
        model.addAttribute("totalReportedCases", totalCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("topDiffCountry", topDiffCountry);
        model.addAttribute("topCasesCountry", topCasesCountry);

        return "barGraph";
    }
}
