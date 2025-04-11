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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CommonStepDefinitions extends MainActions {

    private static final ThreadLocal<GherkinDataTableExtractor> gherkinDataTableExtractor = ThreadLocal.withInitial(GherkinDataTableExtractor::new);
    private static final ThreadLocal<IfStatementHandler> ifStatementHandler = ThreadLocal.withInitial(IfStatementHandler::new);
    private static final BrowserActions browserActions = new BrowserActions();
    private static final MobileActions mobileActions = new MobileActions();

    /**
     * Executes a given scenario by name, including any example data if it’s a Scenario Outline.
     *
     * @param scenarioName The name of the scenario or outline to run
     */
    @When("^run snippet scenario \"([^\"]+)\"$")
    public void runSnippetScenario(String scenarioName) throws Throwable {
        Hooks hooks = new Hooks();
        isSnippet.set(true);
        if (toExecute.get()) {
            Set<Map<String, String>> executedExamples = new HashSet<>(); // Prevent duplicate execution
            List<Map<String, String>> exampleDataList = gherkinDataTableExtractor.get().getExamplesFromScenarioOutline(scenarioName);

            if (!exampleDataList.isEmpty()) {
                for (Map<String, String> exampleData : exampleDataList) {
                    if (exampleData.keySet().equals(new HashSet<>(exampleData.values()))) continue;
                    if (executedExamples.contains(exampleData)) continue;

                    List<List<String>> scenarioStepsForExample = gherkinDataTableExtractor.get().getStepsFromScenario(scenarioName);

                    String formattedExampleData = exampleData.entrySet().stream()
                            .map(entry -> entry.getKey().replaceAll("[<>]", "") + ": " + entry.getValue())
                            .collect(Collectors.joining(", "));

                    ExtentTest parentStep = ExtentManager.getExtent().createNode("Running Scenario: " + scenarioName + " with Example Data: "+formattedExampleData);
                    ExtentManager.setNodeExtent(parentStep);

                    gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioStepsForExample, exampleData, hooks.getScenario());

                    executedExamples.add(exampleData);
                }
                return;
            }
            List<List<String>> scenarioSteps = gherkinDataTableExtractor.get().getStepsFromScenario(scenarioName);
            if (!scenarioSteps.isEmpty()) {
                ExtentTest parentStep = ExtentManager.getExtent().createNode("Running Scenario: " + scenarioName);
                ExtentManager.setNodeExtent(parentStep);

                gherkinDataTableExtractor.get().executeScenarioWithExampleData(scenarioSteps, Collections.emptyMap(), hooks.getScenario());
            }
        }
    }

/**
 * Converts a string to a Boolean value for step definition parameters.
 *
 * @param value The string to convert ("true" or "false")
 * @return The corresponding Boolean value
 */
@ParameterType("true|false")
    public Boolean booleanType(String value) {
        return Boolean.parseBoolean(value);
    }

    /**
     * Executes conditional logic if an element is not visible within the given timeout.
     *
     * @param elementName The name of the element to check
     * @param timeout Optional timeout in seconds
     */
    @And ("^if \"([^\"]+)\" is not visible(?: within (\\d+) seconds)?$")
    public void ifElementIsNotVisible(String elementName, Integer timeout){
        ifStatementHandler.get().toExecuteChecker(new Object(){}.getClass().getEnclosingMethod().getName(), Collections.singletonList(elementName), timeout);
    }

    /**
     * Executes conditional logic if an element is visible within the given timeout.
     *
     * @param elementName The name of the element to check
     * @param timeout Optional timeout in seconds
     */
    @And ("^if \"([^\"]+)\" is visible(?: within (\\d+) seconds)?$")
    public void ifElementIsVisible(String elementName, Integer timeout){
        ifStatementHandler.get().toExecuteChecker(new Object(){}.getClass().getEnclosingMethod().getName(), Collections.singletonList(elementName), timeout);
    }

    /**
     * Ends an "if" statement block.
     */
    @And("end statement")
    public void endIfStatement(){
        ifStatementHandler.get().endIf();
    }

    /**
     * Takes a screenshot of the current screen.
     */
    @And("take screenshot")
    public void takeAScreenshot() {
    }

    /**
     * Launches the specified browser and navigates to the given URL.
     *
     * @param browserType The browser to launch (e.g., chrome, firefox)
     * @param URL The URL to navigate to
     */
    @Given("^launch \"([^\"]+)\" browser and navigate to \"([^\"]+)\"$")
    public void iLaunchTheBrowserAndNavigateTo(String browserType, String URL) {
        if(toExecute.get()){
            browserDriverSetup(browserType, URL);
        }
    }

    /**
     * Launches a remote web browser and navigates to the specified URL.
     * platform options are Parent Key refer from src/test/resources/browserstack.mobile.config.json eg:"chrome", "safari", "firefox", "edge", and "chromium"
     * platform options are Parent Key refer from src/test/resources/browserstack.web.config.json
     * @param URL The target URL
     * @throws IOException if remote driver setup fails
     */
    @Given("^launch web remote browser \"([^\"]+)\" and navigate to \"([^\"]+)\"$")
    public void launchWebRemoteBrowserAndNavigateTo(String parentKey, String URL) throws IOException {
        if(toExecute.get()) {
            remoteDriverSetup(parentKey, URL);
        }
    }

    /**
     * Launches the specified mobile simulator and navigates to the given URL.
     *
     * @param variableURL The URL variable to use for the mobile simulator
     */
    @Given("^launch the Mobile Simulator \"([^\"]+)\"$")
    public void iLaunchTheMobileSimulator(String variableURL) {
        if(toExecute.get()){
            mobileDriverSetup(variableURL);
        }
    }

    /**
     * Navigates the mobile browser to the given URL.
     *
     * @param URL The URL to open
     */
    @And("^navigate mobile browser to \"([^\"]+)\"$")
    public void iNavigateMobileBrowserTo(String URL) {
        if(toExecute.get()) {
            navigateToURL(URL);
        }
    }

    /**
     * Clicks an element identified by its name.
     *
     * @param elementName The name of the element to click
     */
    @When("^click \"([^\"]+)\"$")
    public void iClick(String elementName) {
        if(toExecute.get()){
            click(elementName, null);
        }
    }

    /**
     * Sets text into the specified element.
     *
     * @param value The text to enter
     * @param elementName The target element name
     */
    @When("^set text \"([^\"]+)\" into \"([^\"]+)\"$")
    public void iSetTextInto(String value, String elementName) {
        if(toExecute.get()){
            setText(value, elementName, null);
        }
    }

    /**
     * Retrieves text from an element and stores it in a variable.
     *
     * @param elementName The name of the element to extract text from
     * @param variableName The name of the variable to store the text into
     */
    @Then("^get text from \"([^\"]+)\" and set into variable \"([^\"]+)\"$")
    public void iGetTextFromAndSetIntoVariable(String elementName, String variableName) {
        if(toExecute.get()) {
            variables.get().put(variableName, getText(elementName, null));
        }
    }

    /**
     * Verifies if the expected text is equal to the stored variable text.
     *
     * @param expectedText The expected string value
     * @param variableName The name of the variable to compare
     */
    @Then("^verify text \"([^\"]+)\" is equals to variable \"([^\"]+)\"$")
    public void iVerifyTextIsEqualsToVariable(String expectedText, String variableName) {
        if(toExecute.get()) {
            assertEquals(expectedText, variables.get().get(variableName), "Expected text ["+expectedText+"] is not equals to actual text ["+variables.get().get(variableName) +"]");
        }
    }

    /**
     * Verifies if an element is visible on screen within an optional timeout.
     *
     * @param elementName The name of the element
     * @param timeout Timeout in seconds (optional)
     */
    @Then("^verify \"([^\"]+)\" is visible(?: within (\\d+) seconds)?$")
    public void iVerifyElementIsVisible(String elementName, Integer timeout) {
        if(toExecute.get()) {
            verifyElementVisible(elementName, timeout);
        }
    }

    /**
     * Waits for an element to become visible within the specified timeout.
     *
     * @param elementName The target element
     * @param timeout Timeout in seconds
     */
    @And("^wait for \"([^\"]+)\" to be visible(?: within (\\d+) seconds)?$")
    public void waitForElementToBeVisible(String elementName, Integer timeout) {
        if(toExecute.get()) {
            waitElementVisible(elementName, timeout);
        }
    }

    /**
     * Performs a left swipe action on a mobile device.
     */
    @When("swipe left")
    public void swipeLeft() {
        mobileActions.swipe(MobileActions.SwipeDirection.LEFT, 50);
    }

    /**
     * Verifies the actual text of an element matches the expected text.
     *
     * @param elementName The name of the element
     * @param expectedText The expected text value
     * @param timeout Optional timeout
     */
    @And("^verify actual text of \"([^\"]+)\" is equals to expected text \"([^\"]+)\"(?: within (\\d+) seconds)?$")
    public void verifyTextIsEqualsTo(String elementName, String expectedText, Integer timeout) {
        verifyEquals(getText(elementName, timeout), expectedText);
    }
}
