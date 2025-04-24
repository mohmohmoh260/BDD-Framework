package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import builds.extent.ExtentManager;
import builds.snippet.GherkinDataTableExtractor;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.en.When;

import java.util.*;
import java.util.stream.Collectors;

public class SnippetStepDefinitions extends MainActions {

    private static final ThreadLocal<GherkinDataTableExtractor> gherkinDataTableExtractor = ThreadLocal.withInitial(GherkinDataTableExtractor::new);

    /**
     * Executes a given scenario by name, including any example data if it’s a Scenario Outline.
     *
     * @param scenarioName The name of the scenario or outline to run
     */
    @When("^run snippet scenario \"([^\"]+)\"$")
    public void runSnippetScenario(String scenarioName) throws Throwable {
        if(toExecute.get().getLast()) {
            Set<Map<String, String>> executedExamples = new HashSet<>(); // Prevent duplicate execution
            List<Map<String, String>> exampleDataList = gherkinDataTableExtractor.get().getExamplesFromScenarioOutline(scenarioName);
            isSnippet.set(true);
            if (!exampleDataList.isEmpty()) {
                for (Map<String, String> exampleData : exampleDataList) {
                    if (exampleData.keySet().equals(new HashSet<>(exampleData.values()))) continue;
                    if (executedExamples.contains(exampleData)) continue;

                    List<List<String>> scenarioStepsForExample = gherkinDataTableExtractor.get().getStepsFromScenario(scenarioName);

                    String formattedExampleData = exampleData.entrySet().stream()
                            .map(entry -> entry.getKey().replaceAll("[<>]", "") + ": " + entry.getValue())
                            .collect(Collectors.joining(", "));

                    ExtentManager.setCurrentNodeName("<span style='color:blue'> running snippet scenario: \"" + scenarioName + "\"</span><br>Example Data: " + formattedExampleData);
                    gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioStepsForExample, exampleData);
                    executedExamples.add(exampleData);
                }

                return;
            }
            List<List<String>> scenarioSteps = gherkinDataTableExtractor.get().getStepsFromScenario(scenarioName);
            if (!scenarioSteps.isEmpty()) {
                ExtentManager.setCurrentNodeName("Running Scenario: " + scenarioName);
                gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioSteps, Collections.emptyMap());
            }
        }
    }
}
