package builds.snippet;

import builds.actions.MainActions;
import builds.extent.ExtentManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Scenario;
import workDirectory.stepDefinitions.CommonStepDefinitions;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GherkinDataTableExtractor extends MainActions{

    private static final GherkinStepRunner stepRunner = new GherkinStepRunner(List.of(CommonStepDefinitions.class));

    public List<Map<String, String>> getExamplesFromScenarioOutline(String scenarioName) throws IOException {
        List<Path> featureFiles = getFeatureFiles(); // ✅ Gets all feature files
        List<Map<String, String>> allExamples = new ArrayList<>();

        for (Path featureFile : featureFiles) {
            allExamples.addAll(getExamplesFromScenarioOutline(featureFile, scenarioName)); // ✅ Uses each file
        }

        return allExamples;
    }

    public List<Map<String, String>> getExamplesFromScenarioOutline(Path featureFile, String scenarioName) throws IOException {
        List<String> lines = Files.readAllLines(featureFile);
        boolean foundScenario = false;
        List<String> headers;
        List<Map<String, String>> exampleData = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            // ✅ Detect scenario outline start
            if (line.startsWith("Scenario Outline:") && line.contains(scenarioName)) {
                foundScenario = true;
                continue;
            }

            // ✅ Stop processing when encountering a new scenario or feature
            if (foundScenario && (line.startsWith("Scenario:") || line.startsWith("Scenario Outline:") || line.startsWith("Feature:"))) {
                break; // 🔴 EXIT to avoid processing another scenario's examples
            }

            // ✅ Detect and process Examples table
            if (foundScenario && line.startsWith("Examples:")) {
                i++; // Move to headers row
                headers = extractTableRow(lines.get(i++));

                while (i < lines.size() && lines.get(i).trim().startsWith("|")) {
                    List<String> values = extractTableRow(lines.get(i++));

                    if (values.size() != headers.size()) {
                        throw new IllegalStateException(
                                "❌ Mismatch: headers size (" + headers.size() + ") vs values size (" + values.size() + ") in " + featureFile
                        );
                    }

                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        rowData.put("<" + headers.get(j) + ">", values.get(j));
                    }
                    exampleData.add(rowData);
                }
                break; // ✅ STOP after processing correct Examples table
            }
        }

        return exampleData;
    }

    public List<Path> getFeatureFiles() throws IOException {
        Path featureDirectory = Paths.get("src/test/resources/Snippet"); // Adjust path as needed
        try (Stream<Path> stream = Files.walk(featureDirectory)) {
            return stream.filter(p -> p.toString().endsWith(".feature") && isNotEmpty(p))
                    .collect(Collectors.toList());
        }
    }

    private boolean isNotEmpty(Path path) {
        try {
            return Files.size(path) > 0;
        } catch (IOException e) {
            return false; // Treat unreadable files as empty
        }
    }

    public List<List<String>> getStepsFromScenario(String scenarioName) throws IOException {
        List<Path> featureFiles = getFeatureFiles();
        List<List<String>> allSteps = new ArrayList<>();

        for (Path featureFile : featureFiles) {
            // Ensure exact match when retrieving examples
            List<Map<String, String>> examples = getExamplesFromScenarioOutline(featureFile, scenarioName);

            if (examples.isEmpty()) {
                // Normal scenario (not an outline) - Ensure exact name match
                List<String> steps = extractStepsFromFeature(featureFile, scenarioName, null);

                // Debug: Check if steps were found
                if (!steps.isEmpty()) {
                    allSteps.add(steps);
                }
            } else {
                // Scenario Outline: Extract steps for each example row
                for (Map<String, String> exampleData : examples) {
                    List<String> steps = extractStepsFromFeature(featureFile, scenarioName, exampleData);

                    if (!steps.isEmpty()) {
                        allSteps.add(steps);
                    }
                }
            }
        }

        // Debug: If no steps were found
        if (allSteps.isEmpty()) {
            System.out.println("No exact match found for scenario: " + scenarioName);
        }

        return allSteps.isEmpty() ? Collections.emptyList() : allSteps;
    }

    public List<String> extractStepsFromFeature(Path featureFile, String scenarioName, Map<String, String> exampleData) throws IOException {
        List<String> lines = Files.readAllLines(featureFile);
        boolean isScenarioOutline = false;
        boolean foundScenario = false;
        List<String> stepTemplate = new ArrayList<>();
        List<Map<String, String>> examplesList = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            // ✅ Detect correct scenario
            if (line.matches("^Scenario Outline:\\s+" + Pattern.quote(scenarioName) + "$")) {
                foundScenario = true;
                isScenarioOutline = true;
                stepTemplate.clear();
                continue;
            }
            else if (line.startsWith("Scenario:") || line.startsWith("Scenario Outline:") || line.startsWith("Feature:")) {
                if (foundScenario) break; // ✅ STOP at the next scenario
            }

            // ✅ Capture steps for the intended scenario
            if (foundScenario && line.matches("^(Given|When|Then|And)\\s+.*")) {
                stepTemplate.add(line);
            }

            // ✅ Stop when "Examples:" for another scenario appears
            if (foundScenario && line.startsWith("Examples:")) {
                i++; // Move to headers row
                List<String> headers = extractTableRow(lines.get(i++));

                while (i < lines.size() && lines.get(i).trim().startsWith("|")) {
                    List<String> values = extractTableRow(lines.get(i++));

                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        rowData.put("<" + headers.get(j) + ">", values.get(j));
                    }
                    examplesList.add(rowData);
                }
                break; // ✅ STOP after processing the correct "Examples:"
            }
        }

        // ✅ Handle normal scenarios (not outlines)
        if (!isScenarioOutline) {
            return stepTemplate;
        }

        // ✅ Ensure only the correct example row is used
        for (Map<String, String> row : examplesList) {
            if (row.equals(exampleData)) {
                return stepTemplate.stream()
                        .map(step -> replacePlaceholders(step, row))
                        .toList();
            }
        }

        throw new IllegalArgumentException("No matching example row found for data: " + exampleData);
    }

    private List<String> extractTableRow(String line) {
        return Arrays.stream(line.split("\\|"))
                .map(String::trim)
                .filter(cell -> !cell.isEmpty()) // Removes any accidental empty entries
                .toList();
    }

    private String replacePlaceholders(String step, Map<String, String> exampleValues) {
        for (Map.Entry<String, String> entry : exampleValues.entrySet()) {
            step = step.replace(entry.getKey(), entry.getValue());
        }
        return step;
    }

    public void executeScenarioWithExampleData(List<List<String>> scenarioStepsForExample, Map<String, String> exampleData, Scenario scenario) throws Throwable {

        for (List<String> steps : scenarioStepsForExample) {
            for (String step : steps) {

                // Execute only once with final replaced step
                DataTable dataTable = createDataTable(exampleData);
                boolean status = stepRunner.executeStep(step, dataTable);

                if (status) {
                    if(step.equals("And take screenshot") || (globalDeviceParameter.get().get(0).get("screenshotEveryStep").equals("true"))){
                        ExtentManager.getNodeExtent().pass(step, takeScreenshot());
                    }else{
                        ExtentManager.getNodeExtent().pass(step);
                    }
                } else {
                    ExtentManager.getNodeExtent().fail(step + "<br><br>", takeScreenshot());
                    throw new RuntimeException("ABCDE");
                }
            }
        }
        ExtentManager.removeNodeExtent();
    }

    private DataTable createDataTable(Map<String, String> exampleData) {
        List<List<String>> table = new ArrayList<>();
        table.add(new ArrayList<>(exampleData.keySet())); // Headers
        table.add(new ArrayList<>(exampleData.values())); // Values
        return DataTable.create(table);
    }
}