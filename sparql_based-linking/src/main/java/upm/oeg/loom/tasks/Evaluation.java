package upm.oeg.loom.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wenqi Jiang
 */
public class Evaluation {
  private static final Logger LOGGER = LoggerFactory.getLogger(Evaluation.class);
  private static final String RESULTS_FILE = "tasks/Spaten_LinesTOLines/CONTAINSResults.nt";
  private static final String GOLD_STANDARD_FILE =
      "src/main/resources/Spaten_LinesTOLines/CONTAINS/GoldStandards/CONTAINSmappings.nt";

  public static void main(String[] args) {

    Map<String, String> results = byBufferedReader(RESULTS_FILE);
    Map<String, String> golden = byBufferedReader(GOLD_STANDARD_FILE);
    LOGGER.info("Results: {}", results.size());
    LOGGER.info("Golden: {}", golden.size());
    int target = 0;
    for (Map.Entry<String, String> entry : results.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (golden.containsKey(key) && golden.get(key).equals(value)) {
        target++;
      }
    }

    int missing = 0;
    for (Map.Entry<String, String> entry : golden.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (!results.containsKey(key) || !results.get(key).equals(value)) {
        missing++;
      }
    }

    LOGGER.info(target + " out of " + results.size() + " are correct");
    LOGGER.info(missing + " out of " + golden.size() + " are missing");
  }

  public static Map<String, String> byBufferedReader(String filePath) {
    HashMap<String, String> map = new HashMap<>(100);
    String line;
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      while ((line = reader.readLine()) != null) {
        String[] keyValuePair = line.split("\\s+");
        if (keyValuePair.length > 1) {
          String key = keyValuePair[0];
          String value = keyValuePair[1];
          map.putIfAbsent(key, value);
        } else {
          LOGGER.warn("No Key:Value found in line, ignoring: {}", line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }
}
