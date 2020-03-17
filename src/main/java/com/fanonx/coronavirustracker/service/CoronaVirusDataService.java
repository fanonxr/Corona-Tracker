package com.fanonx.coronavirustracker.service;

import com.fanonx.coronavirustracker.constants.ConstantsUtil;
import com.fanonx.coronavirustracker.models.LocationStats;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CoronaVirusDataService {
  /** Service class to handle getting the data from a URL. */

  private List<LocationStats> allStats = new ArrayList<>();

  /** Method to fetch the data from url.
   * @param dataUrl a url to request the data from.
   * */
  @PostConstruct // run this method as soon as the application runs
  @Scheduled(cron = "* * 1 * * *") // execute this method every day
  public void fetchVirusData() {
    List<LocationStats> newStats = new ArrayList<>(); // to hold the stats of each state
    HttpClient client = HttpClient.newHttpClient();
    // creating a new http request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(ConstantsUtil.VIRUS_CONFIRMED_DATA_URL))
        .build();

    // get a response by having the client send the request
    try {
      HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      // parse the body of the request from csv format to readable format
      StringReader csvBodyReader = new StringReader(httpResponse.body());
      Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
      for (CSVRecord record: records) {
        // create a model with the parsed data
        LocationStats stats = new LocationStats();
        stats.setState(record.get("Province/State"));
        stats.setCountry(record.get("Country/Region"));
        // the lstest day
        int latestCases = Integer.parseInt(record.get(record.size() - 1));
        int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
        stats.setLatestTotalCases(latestCases);
        stats.setDiffFromPreviousDay(prevDayCases);
        // add to new stats
        newStats.add(stats);
      }
      // assign to class array -> we use this array to display the data
      this.allStats = newStats;

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public List<LocationStats> getAllStats() {
    return allStats;
  }
}
