package builds.snippet;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Utility class to check for duplicate scenario names across all .feature files within a given directory.
 * <p>
 * It detects duplicate {@code Scenario} and {@code Scenario Outline} names and halts execution if any are found.
 */
public class FeatureDuplicateChecker {

    /**
     * Scans all .feature files under the given directory (recursively) and checks for duplicate scenario names.
     * If duplicates are found, prints their locations and exits the program with a non-zero status.
     *
     * @param featureDirectory the root directory containing Gherkin feature files
     * @throws IOException if an error occurs reading from the file system
     */
    public static void findDuplicateScenarios(String featureDirectory) throws IOException {
        Map<String, List<String>> scenarioLocations = new HashMap<>();

        // Walk through all feature files in the directory
        try (var paths = Files.walk(Paths.get(featureDirectory))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".feature"))
                    .forEach(path -> processFeatureFile(path, scenarioLocations));
        }

        // Print duplicates and stop execution if found
        checkAndStopExecution(scenarioLocations);
    }

    /**
     * Processes a single feature file and adds each scenario name to the tracking map.
     * Supports both {@code Scenario} and {@code Scenario Outline}.
     *
     * @param featureFile        the path to the feature file
     * @param scenarioLocations  a map tracking scenario names and the files they appear in
     */
    private static void processFeatureFile(Path featureFile, Map<String, List<String>> scenarioLocations) {
        try {
            List<String> lines = Files.readAllLines(featureFile);
            String featureFileName = featureFile.toString();

            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("Scenario:") || line.startsWith("Scenario Outline:")) {
                    String scenarioName = line.replaceFirst("Scenario( Outline)?:", "").trim();

                    scenarioLocations.putIfAbsent(scenarioName, new ArrayList<>());
                    scenarioLocations.get(scenarioName).add(featureFileName);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading file: " + featureFile + " - " + e.getMessage());
        }
    }

    /**
     * Prints all duplicate scenario names and their corresponding locations, then terminates the program.
     * If no duplicates are found, execution continues normally.
     *
     * @param scenarioLocations a map of scenario names to a list of file paths where they appear
     */
    private static void checkAndStopExecution(Map<String, List<String>> scenarioLocations) {
        boolean hasDuplicates = false;
        for (Map.Entry<String, List<String>> entry : scenarioLocations.entrySet()) {
            if (entry.getValue().size() > 1) {
                hasDuplicates = true;
                System.err.println("Duplicate Scenario Found: \"" + entry.getKey() + "\"");
                for (String location : entry.getValue()) {
                    System.err.println("   • Found in: " + location);
                }
            }
        }

        // Stop execution if duplicates are found
        if (hasDuplicates) {
            System.exit(1);
        }
    }
}