package workDirectory.stepDefinitions;

import builds.snippet.GherkinDataTableExtractor;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CommonStepDefinitions extends CommonMethods{

    GherkinDataTableExtractor gherkinDataTableExtractor = new GherkinDataTableExtractor();

    @When("^run snippet scenario \"([^\"]+)\"$")
    public void runSnippetScenario(String scenarioName) throws Exception {
        List<Path> featureFiles = gherkinDataTableExtractor.getFeatureFiles();
        Set<Map<String, String>> executedExamples = new HashSet<>(); // Prevent duplicate execution

        for (Path featureFile : featureFiles) {
            // Fetch examples for the Scenario Outline
            List<Map<String, String>> exampleDataList = gherkinDataTableExtractor.getExamplesFromScenarioOutline(featureFile, scenarioName);

            if (!exampleDataList.isEmpty()) {
                for (Map<String, String> exampleData : exampleDataList) {
                    // ✅ Prevent duplicate execution of the same example
                    if (executedExamples.contains(exampleData)) {
                        continue;
                    }

                    List<List<String>> scenarioStepsForExample = Collections.singletonList(gherkinDataTableExtractor.extractStepsFromFeature(featureFile, scenarioName, exampleData));

                    String formattedExampleData = exampleData.entrySet().stream()
                            .map(entry -> entry.getKey().replaceAll("[<>]", "") + ": " + entry.getValue())
                            .collect(Collectors.joining(", "));

                    currentScenario.get().log("🔹 Running Scenario: **" + scenarioName + "** with Example Data: {" + formattedExampleData + "}");

                    gherkinDataTableExtractor.executeScenarioWithExampleData(scenarioStepsForExample, exampleData, currentScenario.get());

                    executedExamples.add(exampleData); // ✅ Mark this example as executed
                }
                return; // Exit after executing Scenario Outline examples
            }

            // ✅ If no examples exist, run the scenario normally
            List<List<String>> scenarioSteps = gherkinDataTableExtractor.getStepsFromScenario(scenarioName);
            if (!scenarioSteps.isEmpty()) {
                currentScenario.get().log("🔹 Running Scenario: **" + scenarioName + "** (No Examples)");

                try {
                    gherkinDataTableExtractor.executeScenarioWithExampleData(scenarioSteps, Collections.emptyMap(), currentScenario.get());
                } catch (Exception e) {
                    currentScenario.get().log("❌ Scenario Failed: **" + scenarioName + "** | Error: " + e.getMessage());
                    throw e;
                }
                return; // Exit after executing the scenario
            }
        }
    }

    // Adding addition parameter for gherkin
    @ParameterType("true|false")
    public Boolean booleanType(String value) {
        return Boolean.parseBoolean(value);
    }

    @And ("^if \"([^\"]+)\" is not visible(?: within (\\d+) seconds)?$")
    public void ifElementIsNotVisible(String elementName, Integer timeout){
        toExecuteChecker(new Object(){}.getClass().getEnclosingMethod().getName(), Collections.singletonList(elementName), timeout);
    }

    @And("end statement")
    public void endIfStatement(){
        endIf();
    }

    @And("take screenshot")
    public void takeAScreenshot() {
        if(toExecute.get()){
            takeScreenshot();
        }
    }

    @Given("^launch the Mobile Simulator \"([^\"]+)\"$")
    public void iLaunchTheMobileSimulator(String variableURL) {
        if(toExecute.get()){
            mobileSetup(variableURL);
        }
    }

    @Given("^launch \"([^\"]+)\" browser and navigate to \"([^\"]+)\"$")
    public void iLaunchTheBrowserAndNavigateTo(String browserType, String URL) {
        if(toExecute.get()){
            browserSetup(browserType, URL);
        }
    }

    @And("^navigate mobile browser to \"([^\"]+)\"$")
    public void iNavigateMobileBrowserTo(String URL) {
        if(toExecute.get()) {
            navigateToURL(URL);
        }
    }

    @When("^click \"([^\"]+)\"$")
    public void iClick(String elementName) {
        if(toExecute.get()){
            click(elementName);
        }
    }

    @When("^set text \"([^\"]+)\" into \"([^\"]+)\"$")
    public void iSetTextInto(String value, String elementName) {
        if(toExecute.get()){
            setText(value, elementName);
        }
    }

    @Then("^get text from \"([^\"]+)\" and set into variable \"([^\"]+)\"$")
    public void iGetTextFromAndSetIntoVariable(String elementName, String variableName) {
        if(toExecute.get()) {
            getTextFromAndSetIntoVariable(elementName, variableName, Integer.parseInt(globalDeviceParameter.get(0).get("timeOut")));
        }
    }

    @Then("^verify text \"([^\"]+)\" is equals to variable \"([^\"]+)\"$")
    public void iVerifyTextIsEqualsToVariable(String expectedText, String variableName) {
        if(toExecute.get()) {
            verifyTextIsEqualsToVariable(expectedText, variableName);
        }
    }

    @Then("^verify element \"([^\"]+)\" is visible(?: within (\\d+) seconds)?$")
    public void iVerifyElementIsVisible(String elementName, Integer timeout) {
        if(toExecute.get()) {
            verifyElementVisible(elementName, timeout);
        }
    }

    @And("^wait for element \"([^\"]+)\" to be visible(?: within (\\d+) seconds)?$")
    public void waitForElementToBeVisible(String elementName, Integer timeout) {
        if(toExecute.get()) {
            waitElementVisible(elementName, timeout);
        }
    }
}
