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
    List<LocationStats> allStats = coronaVirusDataService.getAllStats();
    int totalConfirmedCases = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
    int totalNewCases = allStats.stream().mapToInt(LocationStats::getDiffFromPreviousDay).sum();
    model.addAttribute("locationStats", allStats);
    model.addAttribute("totalReportedCases", totalConfirmedCases);
    model.addAttribute("totalNewCases", totalNewCases);
    return "home";
  }
}
