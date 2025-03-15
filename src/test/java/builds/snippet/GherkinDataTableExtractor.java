package builds.snippet;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Scenario;
import workDirectory.stepDefinitions.CommonStepDefinitions;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class GherkinDataTableExtractor {

    private final GherkinStepRunner stepRunner = new GherkinStepRunner(List.of(CommonStepDefinitions.class));

    public List<Map<String, String>> getExamplesFromScenarioOutline(Path featureFile, String scenarioName) throws IOException {
        List<String> lines = Files.readAllLines(featureFile);
        boolean foundScenario = false;
        boolean foundExamples = false;
        List<String> headers = new ArrayList<>();
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
                foundExamples = true;
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

    public List<List<String>> getStepsFromScenario(String scenarioName) throws IOException {
        List<Path> featureFiles = getFeatureFiles();

        for (Path featureFile : featureFiles) {
            // Pass null to get all example variations
            return Collections.singletonList(extractStepsFromFeature(featureFile, scenarioName, null)); // Return immediately to prevent duplicate retrieval
        }
        return Collections.emptyList();
    }

    public List<Path> getFeatureFiles() throws IOException {
        try (var paths = Files.walk(Paths.get("src/test/resources/Snippet"))) {
            return paths.filter(path -> {
                boolean isFile = Files.isRegularFile(path);
                return isFile && path.toString().endsWith(".feature");
            }).toList();
        }
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
            if (line.startsWith("Scenario Outline:") && line.contains(scenarioName)) {
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

                    if (values.size() != headers.size()) {  // ✅ ADD THIS CHECK
                        throw new IllegalStateException(
                                "❌ Mismatch: headers size (" + headers.size() + ") vs values size (" + values.size() + ") in " + featureFile
                        );
                    }

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
                boolean passed = stepRunner.executeStep(step, dataTable);

                if (passed) {
                    scenario.log("✅ Passed: " + step);
                } else {
                    scenario.log("❌ Failed: " + step);
                }
            }
        }
    }

    private DataTable createDataTable(Map<String, String> exampleData) {
        List<List<String>> table = new ArrayList<>();
        table.add(new ArrayList<>(exampleData.keySet())); // Headers
        table.add(new ArrayList<>(exampleData.values())); // Values
        return DataTable.create(table);
    }
}