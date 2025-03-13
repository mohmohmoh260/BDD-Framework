package workDirectory.stepDefinitions;

import builds.snippet.GherkinDataTableExtractor;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CommonStepDefinitions extends CommonMethods{

    GherkinDataTableExtractor gherkinDataTableExtractor = new GherkinDataTableExtractor();

    @Before
    public void beforeScenario(Scenario scenario) {
        currentScenario.set(scenario); // Capture the scenario instance before each scenario runs
    }

    @When("run snippet scenario {string}")
    public void runSnippetScenario(String scenarioName) throws Exception {
        List<Path> featureFiles = GherkinDataTableExtractor.getFeatureFiles();
        Set<Map<String, String>> executedExamples = new HashSet<>(); // Prevent duplicate execution

        for (Path featureFile : featureFiles) {
            // Fetch examples for the Scenario Outline
            List<Map<String, String>> exampleDataList = GherkinDataTableExtractor.getExamplesFromScenarioOutline(featureFile, scenarioName);

            if (!exampleDataList.isEmpty()) {
                for (Map<String, String> exampleData : exampleDataList) {
                    // ✅ Prevent duplicate execution of the same example
                    if (executedExamples.contains(exampleData)) {
                        continue;
                    }

                    List<List<String>> scenarioStepsForExample = Collections.singletonList(GherkinDataTableExtractor.extractStepsFromFeature(featureFile, scenarioName, exampleData));

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
            List<List<String>> scenarioSteps = GherkinDataTableExtractor.getStepsFromScenario(scenarioName);
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

    @And ("if {int} is bigger than {int}")
    public void ifNumberIsBiggerThanNumber(int firstValue, int secondValue){
       addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Arrays.asList(firstValue,secondValue));
    }

    @And ("if {int} is smaller than {int}")
    public void ifNumberIsSmallerThanNumber(int firstValue, int secondValue){
        addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Arrays.asList(firstValue,secondValue));
    }

    @And ("if {string} is not visible")
    public void ifElementIsNotVisible(String elementName){
        addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Collections.singletonList(elementName));
    }

    @And("end statement")
    public void endIfStatement(){
        endIf();
    }

    @Then("print from data table without header below")
    public void printFromDataTableWithoutHeaderBelow(DataTable dataTable) {
        System.out.println(dataTable.cells());
    }

    @Then("print from data table with header below")
    public void printFromDataTableWithHeaderBelow(DataTable dataTable) {
        System.out.println(dataTable.cells());
    }

    @Then("print string {string} {double} {booleanType}")
    public void printString(String arg0, Double arg1, Boolean arg2) {
        if(toExecute.get()){
            System.out.println(arg0);
        }

    }

    @Given("I launch the Mobile Simulator {string}")
    public void iLaunchTheMobileSimulator(String variableURL) {
        if(toExecute.get()){
            mobileSetup(variableURL);
        }
    }

    @Given("I navigate mobile browser to {string}")
    public void iNavigateMobileBrowserTo(String URL) {
        if(toExecute.get()) {
            navigateToURL(URL);
        }
    }

    @When("I set text {string} into {string}")
    public void iSetTextInto(String value, String elementName) {
        if(toExecute.get()){
            setText(value, elementName);
        }
    }

    @And("take screenshot")
    public void takeAScreenshot() {
        if(toExecute.get()){
           takeScreenshot();
        }
    }

    @Given("I launch {string} browser and navigate to {string}")
    public void iLaunchTheBrowserAndNavigateTo(String browserType, String URL) {
        if(toExecute.get()){
           browserSetup(browserType, URL);
        }
    }

    @When("I click {string}")
    public void iClick(String elementName) {
        if(toExecute.get()){
           click(elementName);
        }
    }

    @Then("I verify element {string} is visible")
    public void iVerifyElementIsVisible(String elementName) {
        if(toExecute.get()) {
            verifyElementVisible(elementName);
        }
    }

    @Then("I get text from {string} and set into variable {string}")
    public void iGetTextFromAndSetIntoVariable(String elementName, String variableName) {
        if(toExecute.get()) {
            getTextFromAndSetIntoVariable(elementName, variableName);
        }
    }

    @Then("I verify text {string} is equals to variable {string}")
    public void iVerifyTextIsEqualsToVariable(String expectedText, String variableName) {
        if(toExecute.get()) {
            verifyTextIsEqualsToVariable(expectedText, variableName);
        }
    }
}
