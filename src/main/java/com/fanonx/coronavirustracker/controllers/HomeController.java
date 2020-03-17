package com.fanonx.coronavirustracker.controllers;

import com.fanonx.coronavirustracker.models.LocationStats;
import com.fanonx.coronavirustracker.service.CoronaVirusDataService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  /** Controller class to generate/render the html UI */
  @Autowired
  CoronaVirusDataService coronaVirusDataService;

  @GetMapping("/") // map this to the root template
  public String home(Model model) {
    List<LocationStats> allStats = coronaVirusDataService.getAllConfirmedStats();
    List<LocationStats> allDeathStats = coronaVirusDataService.getAllDeathsStats();
    List<LocationStats> allRecoveredStats = coronaVirusDataService.getAllRecoveredStats();

    // get the total confirmed cases
    int totalConfirmedCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
    int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPreviousDay).sum();

    int totalDeaths = allDeathStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
    int totalNewDeaths = allDeathStats.stream().mapToInt(LocationStats::getDiffFromPreviousDay).sum();

    int totalRecovered = allRecoveredStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
    int totalNewRecovered = allRecoveredStats.stream().mapToInt(LocationStats::getDiffFromPreviousDay).sum();

    // send the models to the view
    model.addAttribute("locationStats", allStats);
    model.addAttribute("totalReportedCases", totalConfirmedCases);
    model.addAttribute("totalNewCases", totalNewCases);

    model.addAttribute("deathStats", allDeathStats);
    model.addAttribute("totalDeaths", totalDeaths);
    model.addAttribute("totalNewDeaths", totalNewDeaths);

    model.addAttribute("recoveredStats", allRecoveredStats);
    model.addAttribute("totalRecoveries", totalRecovered);
    model.addAttribute("totalNewRecoveries", totalNewRecovered);


    return "home";
  }
}
