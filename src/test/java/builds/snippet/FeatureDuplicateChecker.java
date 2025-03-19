package builds.snippet;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FeatureDuplicateChecker {

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