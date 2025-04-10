package workDirectory.stepDefinitions;

import builds.actions.BrowserActions;
import builds.actions.MainActions;
import builds.actions.MobileActions;
import builds.extent.ExtentManager;
import builds.snippet.GherkinDataTableExtractor;
import builds.utilities.IfStatementHandler;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CommonStepDefinitions extends MainActions {

    private static final ThreadLocal<GherkinDataTableExtractor> gherkinDataTableExtractor = ThreadLocal.withInitial(GherkinDataTableExtractor::new);
    private static final ThreadLocal<IfStatementHandler> ifStatementHandler = ThreadLocal.withInitial(IfStatementHandler::new);
    private static final BrowserActions browserActions = new BrowserActions();
    private static final MobileActions mobileActions = new MobileActions();
    private static final SoftAssert softAssert = new SoftAssert();

    @When("^run snippet scenario \"([^\"]+)\"$")
    public void runSnippetScenario(String scenarioName) throws Throwable {
        Hooks hooks = new Hooks();
        if (toExecute.get()) {
            Set<Map<String, String>> executedExamples = new HashSet<>(); // Prevent duplicate execution

            // Fetch examples from ALL feature files
            List<Map<String, String>> exampleDataList = gherkinDataTableExtractor.get().getExamplesFromScenarioOutline(scenarioName);

            if (!exampleDataList.isEmpty()) {
                for (Map<String, String> exampleData : exampleDataList) {
                    if (executedExamples.contains(exampleData)) continue;

                    List<List<String>> scenarioStepsForExample = gherkinDataTableExtractor.get().getStepsFromScenario(scenarioName);

                    String formattedExampleData = exampleData.entrySet().stream()
                            .map(entry -> entry.getKey().replaceAll("[<>]", "") + ": " + entry.getValue())
                            .collect(Collectors.joining(", "));

                    ExtentTest parentStep = ExtentManager.getExtent().createNode("Running Scenario: " + scenarioName + " with Example Data: [" + formattedExampleData + "]");
                    ExtentManager.setNodeExtent(parentStep);

                    gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioStepsForExample, exampleData, hooks.getScenario());

                    executedExamples.add(exampleData);
                }
                return;
            }

            // 🔹 Handle non-outline scenarios
            List<List<String>> scenarioSteps = gherkinDataTableExtractor.get().getStepsFromScenario(scenarioName);
            if (!scenarioSteps.isEmpty()) {
                ExtentTest parentStep = ExtentManager.getExtent().createNode("Running Scenario: " + scenarioName);
                ExtentManager.setNodeExtent(parentStep);

                gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioSteps, Collections.emptyMap(), hooks.getScenario());
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

    @And ("^if \"([^\"]+)\" is visible(?: within (\\d+) seconds)?$")
    public void ifElementIsVisible(String elementName, Integer timeout){
        ifStatementHandler.get().toExecuteChecker(new Object(){}.getClass().getEnclosingMethod().getName(), Collections.singletonList(elementName), timeout);
    }

    @And("end statement")
    public void endIfStatement(){
        ifStatementHandler.get().endIf();
    }

    @And("take screenshot")
    public void takeAScreenshot() {
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
            //hooks.getScenario().log("Text :"+getText(elementName, null)+" is set into variable "+variableName);
        }
    }

    @Then("^verify text \"([^\"]+)\" is equals to variable \"([^\"]+)\"$")
    public void iVerifyTextIsEqualsToVariable(String expectedText, String variableName) {
        if(toExecute.get()) {
          assertEquals(expectedText, variables.get().get(variableName), "Expected text "+expectedText+" is not equals to actual text "+variables.get().get(variableName));
        }
    }

    @Then("^verify \"([^\"]+)\" is visible(?: within (\\d+) seconds)?$")
    public void iVerifyElementIsVisible(String elementName, Integer timeout) {
        if(toExecute.get()) {
            verifyElementVisible(elementName, timeout);
        }
    }

    @And("^wait for \"([^\"]+)\" to be visible(?: within (\\d+) seconds)?$")
    public void waitForElementToBeVisible(String elementName, Integer timeout) {
        if(toExecute.get()) {
            waitElementVisible(elementName, timeout);
        }
    }

    @When("swipe left")
    public void swipeLeft() {
        mobileActions.swipe(MobileActions.SwipeDirection.LEFT, 50);
    }

    @And("^verify actual text of \"([^\"]+)\" is equals to expected text \"([^\"]+)\"(?: within (\\d+) seconds)?$")
    public void verifyTextIsEqualsTo(String elementName, String expectedText, Integer timeout) {
        verifyEquals(getText(elementName, timeout), expectedText);
    }

    @And("^print \"([^\"]+)\"$")
    public void print(String arg0) {
        System.out.println(arg0);
    }
}
