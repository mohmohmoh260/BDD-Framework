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

/**
 * Utility class for extracting and executing Gherkin scenario steps, particularly Scenario Outlines with Examples.
 */
public class GherkinDataTableExtractor extends MainActions {

    private static final GherkinStepRunner stepRunner = new GherkinStepRunner(List.of(CommonStepDefinitions.class));

    /**
     * Retrieves all example data rows for a specific scenario outline name across all feature files.
     *
     * @param scenarioName the name of the scenario outline
     * @return a list of maps representing example data rows, where each map contains parameter names and values
     * @throws IOException if an I/O error occurs reading from the feature files
     */
    public List<Map<String, String>> getExamplesFromScenarioOutline(String scenarioName) throws IOException {
        List<Path> featureFiles = getFeatureFiles();
        List<Map<String, String>> allExamples = new ArrayList<>();

        for (Path featureFile : featureFiles) {
            allExamples.addAll(getExamplesFromScenarioOutline(featureFile, scenarioName));
        }

        return allExamples;
    }

    /**
     * Retrieves example data rows for a scenario outline with a given name from a specific feature file.
     *
     * @param featureFile  the path to the feature file
     * @param scenarioName the name of the scenario outline
     * @return a list of maps containing example values keyed by placeholder
     * @throws IOException if an I/O error occurs reading the file
     */
    public List<Map<String, String>> getExamplesFromScenarioOutline(Path featureFile, String scenarioName) throws IOException {
        List<String> lines = Files.readAllLines(featureFile);
        boolean foundScenario = false;
        List<String> headers;
        List<Map<String, String>> exampleData = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.startsWith("Scenario Outline:") && line.contains(scenarioName)) {
                foundScenario = true;
                continue;
            }

            if (foundScenario && (line.startsWith("Scenario:") || line.startsWith("Scenario Outline:") || line.startsWith("Feature:"))) {
                break;
            }

            if (foundScenario && line.startsWith("Examples:")) {
                i++;
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
                break;
            }
        }

        return exampleData;
    }

    /**
     * Recursively finds all non-empty .feature files in the target feature directory.
     *
     * @return a list of paths to feature files
     * @throws IOException if an I/O error occurs during directory traversal
     */
    public List<Path> getFeatureFiles() throws IOException {
        Path featureDirectory = Paths.get("src/test/resources/Snippet");
        try (Stream<Path> stream = Files.walk(featureDirectory)) {
            return stream.filter(p -> p.toString().endsWith(".feature") && isNotEmpty(p))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Checks whether a given file path points to a non-empty file.
     *
     * @param path the file path
     * @return true if the file is not empty; false otherwise
     */
    private boolean isNotEmpty(Path path) {
        try {
            return Files.size(path) > 0;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Extracts and resolves steps for a scenario (or scenario outline with resolved examples).
     *
     * @param scenarioName the name of the scenario
     * @return a list of resolved step lists (one list per example if it's an outline)
     * @throws IOException if an I/O error occurs during file reading
     */
    public List<List<String>> getStepsFromScenario(String scenarioName) throws IOException {
        List<Path> featureFiles = getFeatureFiles();
        List<List<String>> allSteps = new ArrayList<>();

        for (Path featureFile : featureFiles) {
            List<Map<String, String>> examples = getExamplesFromScenarioOutline(featureFile, scenarioName);

            if (examples.isEmpty()) {
                List<String> steps = extractStepsFromFeature(featureFile, scenarioName, null);
                if (!steps.isEmpty()) {
                    allSteps.add(steps);
                }
            } else {
                for (Map<String, String> exampleData : examples) {
                    List<String> steps = extractStepsFromFeature(featureFile, scenarioName, exampleData);
                    if (!steps.isEmpty()) {
                        allSteps.add(steps);
                    }
                }
            }
        }

        if (allSteps.isEmpty()) {
            System.out.println("No exact match found for scenario: " + scenarioName);
        }

        return allSteps.isEmpty() ? Collections.emptyList() : allSteps;
    }

    /**
     * Extracts steps for a given scenario from a feature file and optionally replaces outline placeholders with example data.
     *
     * @param featureFile  the feature file path
     * @param scenarioName the scenario name
     * @param exampleData  the example data row for scenario outlines (null if not applicable)
     * @return a list of steps, with placeholders replaced if example data is provided
     * @throws IOException if reading the file fails
     */
    public List<String> extractStepsFromFeature(Path featureFile, String scenarioName, Map<String, String> exampleData) throws IOException {
        List<String> lines = Files.readAllLines(featureFile);
        boolean isScenarioOutline = false;
        boolean foundScenario = false;
        List<String> stepTemplate = new ArrayList<>();
        List<Map<String, String>> examplesList = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.matches("^Scenario Outline:\\s+" + Pattern.quote(scenarioName) + "$")) {
                foundScenario = true;
                isScenarioOutline = true;
                stepTemplate.clear();
                continue;
            } else if (line.startsWith("Scenario:") || line.startsWith("Scenario Outline:") || line.startsWith("Feature:")) {
                if (foundScenario) break;
            }

            if (foundScenario && line.matches("^(Given|When|Then|And)\\s+.*")) {
                stepTemplate.add(line);
            }

            if (foundScenario && line.startsWith("Examples:")) {
                i++;
                List<String> headers = extractTableRow(lines.get(i++));

                while (i < lines.size() && lines.get(i).trim().startsWith("|")) {
                    List<String> values = extractTableRow(lines.get(i++));

                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        rowData.put("<" + headers.get(j) + ">", values.get(j));
                    }
                    examplesList.add(rowData);
                }
                break;
            }
        }

        if (!isScenarioOutline) {
            return stepTemplate;
        }

        for (Map<String, String> row : examplesList) {
            if (row.equals(exampleData)) {
                return stepTemplate.stream()
                        .map(step -> replacePlaceholders(step, row))
                        .toList();
            }
        }

        throw new IllegalArgumentException("No matching example row found for data: " + exampleData);
    }

    /**
     * Extracts a row from a Gherkin-style pipe-separated table.
     *
     * @param line a line of text containing table row
     * @return a list of cell values from the row
     */
    private List<String> extractTableRow(String line) {
        return Arrays.stream(line.split("\\|"))
                .map(String::trim)
                .filter(cell -> !cell.isEmpty())
                .toList();
    }

    /**
     * Replaces scenario outline placeholders with actual values from a given example row.
     *
     * @param step          the step containing placeholders
     * @param exampleValues a map of placeholder to actual value
     * @return the resolved step with placeholders replaced
     */
    private String replacePlaceholders(String step, Map<String, String> exampleValues) {
        for (Map.Entry<String, String> entry : exampleValues.entrySet()) {
            step = step.replace(entry.getKey(), entry.getValue());
        }
        return step;
    }

    /**
     * Executes each step of a scenario using the resolved example data and logs results using Extent Reports.
     *
     * @param scenarioStepsForExample the list of steps to execute
     * @param exampleData             the example data to convert into a DataTable
     * @param scenario                the Cucumber Scenario object
     * @throws Throwable if a step execution fails
     */
    public void executeScenarioWithExampleData(List<List<String>> scenarioStepsForExample, Map<String, String> exampleData, Scenario scenario) throws Throwable {
        for (List<String> steps : scenarioStepsForExample) {
            for (String step : steps) {
                DataTable dataTable = createDataTable(exampleData);
                boolean status = stepRunner.executeStep(step, dataTable);

                if (status) {
                    if (step.equals("And take screenshot") || (globalDeviceParameter.get().get(0).get("screenshotEveryStep").equals("true"))) {
                        ExtentManager.getNodeExtent().pass(step, takeScreenshot());
                    } else {
                        ExtentManager.getNodeExtent().pass(step);
                    }
                } else {
                    ExtentManager.getNodeExtent().fail(step + "<br><br>", takeScreenshot());
                    throw new RuntimeException("Method invocation error");
                }
            }
        }
        ExtentManager.removeNodeExtent();
    }

    /**
     * Converts example data into a Cucumber {@link DataTable} format.
     *
     * @param exampleData the data map of parameter names and values
     * @return a DataTable with headers and values
     */
    private DataTable createDataTable(Map<String, String> exampleData) {
        List<List<String>> table = new ArrayList<>();
        table.add(new ArrayList<>(exampleData.keySet()));
        table.add(new ArrayList<>(exampleData.values()));
        return DataTable.create(table);
    }
}