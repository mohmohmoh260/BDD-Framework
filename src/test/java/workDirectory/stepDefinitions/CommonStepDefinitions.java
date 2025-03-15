package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import builds.snippet.GherkinDataTableExtractor;
import builds.utilities.IfStatementHandler;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CommonStepDefinitions extends MainActions {

    private static final ThreadLocal<GherkinDataTableExtractor> gherkinDataTableExtractor = ThreadLocal.withInitial(GherkinDataTableExtractor::new);
    private static final ThreadLocal<IfStatementHandler> ifStatementHandler = ThreadLocal.withInitial(IfStatementHandler::new);

    @When("^run snippet scenario \"([^\"]+)\"$")
    public void runSnippetScenario(String scenarioName) throws Throwable {
        List<Path> featureFiles = gherkinDataTableExtractor.get().getFeatureFiles();
        Set<Map<String, String>> executedExamples = new HashSet<>(); // Prevent duplicate execution

        for (Path featureFile : featureFiles) {
            // Fetch examples for the Scenario Outline
            List<Map<String, String>> exampleDataList = gherkinDataTableExtractor.get().getExamplesFromScenarioOutline(featureFile, scenarioName);

            if (!exampleDataList.isEmpty()) {
                for (Map<String, String> exampleData : exampleDataList) {
                    // ✅ Prevent duplicate execution of the same example
                    if (executedExamples.contains(exampleData)) {
                        continue;
                    }

                    // Extract steps for the specific example only
                    List<List<String>> scenarioStepsForExample = Collections.singletonList(
                            gherkinDataTableExtractor.get().extractStepsFromFeature(featureFile, scenarioName, exampleData)
                    );

                    String formattedExampleData = exampleData.entrySet().stream()
                            .map(entry -> entry.getKey().replaceAll("[<>]", "") + ": " + entry.getValue())
                            .collect(Collectors.joining(", "));

                    Hooks.getScenario().log("🔹 Running Scenario: **" + scenarioName + "** with Example Data: {" + formattedExampleData + "}");

                    gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioStepsForExample, exampleData, Hooks.getScenario());

                    executedExamples.add(exampleData); // ✅ Mark this example as executed
                }
                return; // ✅ Exit after executing Scenario Outline examples
            }

            // ✅ If no examples exist, run the scenario normally (single execution)
            List<List<String>> scenarioSteps = gherkinDataTableExtractor.get().getStepsFromScenario(scenarioName);
            if (!scenarioSteps.isEmpty()) {
                Hooks.getScenario().log("🔹 Running Scenario: **" + scenarioName + "** (No Examples)");

                try {
                    gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioSteps, Collections.emptyMap(), Hooks.getScenario());
                } catch (Exception e) {
                    Hooks.getScenario().log("❌ Scenario Failed: **" + scenarioName + "** | Error: " + e.getMessage());
                    throw e;
                }
                return; // ✅ Exit after executing the scenario
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
        ifStatementHandler.get().toExecuteChecker(new Object(){}.getClass().getEnclosingMethod().getName(), Collections.singletonList(elementName), timeout);
    }

    @And("end statement")
    public void endIfStatement(){
        ifStatementHandler.get().endIf();
    }

    @And("take screenshot")
    public void takeAScreenshot() {
        if(toExecute.get()){
            screenshot();
        }
    }

    @Given("^launch \"([^\"]+)\" browser and navigate to \"([^\"]+)\"$")
    public void iLaunchTheBrowserAndNavigateTo(String browserType, String URL) {
        if(toExecute.get()){
            browserDriverSetup(browserType, URL);
        }
    }

    @Given("^launch web remote browser and navigate to \"([^\"]+)\"$")
    public void launchWebRemoteBrowserAndNavigateTo(String URL) throws IOException {
        if(toExecute.get()) {
            remoteDriverSetup("web", URL);
        }
    }

    @Given("^launch the Mobile Simulator \"([^\"]+)\"$")
    public void iLaunchTheMobileSimulator(String variableURL) {
        if(toExecute.get()){
            mobileDriverSetup(variableURL);
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
            click(elementName, null);
        }
    }

    @When("^set text \"([^\"]+)\" into \"([^\"]+)\"$")
    public void iSetTextInto(String value, String elementName) {
        if(toExecute.get()){
            setText(value, elementName, null);
        }
    }

    @Then("^get text from \"([^\"]+)\" and set into variable \"([^\"]+)\"$")
    public void iGetTextFromAndSetIntoVariable(String elementName, String variableName) {
        if(toExecute.get()) {
            variables.get().put(variableName, getText(elementName, null));
            Hooks.getScenario().log("Text :"+getText(elementName, null)+" is set into variable "+variableName);
        }
    }

    @Then("^verify text \"([^\"]+)\" is equals to variable \"([^\"]+)\"$")
    public void iVerifyTextIsEqualsToVariable(String expectedText, String variableName) {
        if(toExecute.get()) {
            Assert.assertEquals(expectedText, variables.get().get(variableName), "Expected text "+expectedText+" is not equals to actual text "+variables.get().get(variableName));
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


    @Then("^print \"([^\"]+)\" and \"([^\"]+)\"$")
    public void printAnd(String arg0, String arg1) {
        System.out.println(arg0+" "+arg1);
    }
}
