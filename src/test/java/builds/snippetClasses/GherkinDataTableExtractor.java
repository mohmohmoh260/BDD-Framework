package builds.snippetClasses;

import io.cucumber.datatable.DataTable;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class GherkinDataTableExtractor {

    public static List<String> getStepsFromScenario(String scenarioName) throws IOException {
        List<Path> featureFiles = getFeatureFiles();

        for (Path featureFile : featureFiles) {
            List<String> steps = extractStepsFromFeature(featureFile, scenarioName);
            if (!steps.isEmpty()) {
                return steps;
            }
        }
        return Collections.emptyList(); // No scenario found
    }

    private static List<Path> getFeatureFiles() throws IOException {
        try (var paths = Files.walk(Paths.get("src/test/resources/Snippet"))) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".feature"))
                    .toList();
        }
    }

    private static List<String> extractStepsFromFeature(Path featureFile, String scenarioName) throws IOException {
        List<String> lines = Files.readAllLines(featureFile);
        boolean foundScenario = false;
        List<String> steps = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Scenario:") && line.contains(scenarioName)) {
                foundScenario = true;
                continue;
            }
            if (foundScenario) {
                if (line.startsWith("Scenario:") || line.startsWith("Feature:")) {
                    break; // Stop if we reach another scenario or a new feature
                }
                if (line.matches("^(Given|When|Then|And)\\s+.*")) {
                    steps.add(line);
                }
            }
        }
        return steps;
    }

    public static DataTable getDataTableFromFeature(String gherkinSentence) throws IOException {
        String cleanedStep = cleanGherkinStep(gherkinSentence);
        List<Path> featureFiles = getFeatureFiles();

        for (Path featureFile : featureFiles) {
            DataTable table = extractDataTableFromFeature(featureFile, cleanedStep);
            if (table != null) {
                return table;
            }
        }
        return null; // Return null if no DataTable found
    }

    private static String cleanGherkinStep(String step) {
        return step.replaceAll("^(Given|When|Then|And)\\s+", "").trim();
    }

    private static DataTable extractDataTableFromFeature(Path featureFile, String cleanedStep) throws IOException {
        List<String> lines = Files.readAllLines(featureFile);
        boolean foundStep = false;
        List<List<String>> tableRows = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (cleanGherkinStep(line).equalsIgnoreCase(cleanedStep)) {
                foundStep = true;
                continue;
            }
            if (foundStep) {
                if (line.startsWith("|")) {
                    List<String> row = Arrays.asList(line.replace("|", "").trim().split("\\s*\\|\\s*"));
                    tableRows.add(row);
                } else {
                    break; // Stop if we reach another step or section
                }
            }
        }
        return tableRows.isEmpty() ? null : DataTable.create(tableRows);
    }
}