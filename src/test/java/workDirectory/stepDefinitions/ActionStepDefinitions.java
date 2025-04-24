package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import builds.extent.ExtentManager;
import builds.listener.StepListener;
import com.aventstack.extentreports.Status;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;

public class ActionStepDefinitions extends MainActions {
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
    public void launchTheBrowserAndNavigateTo(String browserType, String URL) {
        if(toExecute.get().getLast()) {
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
        if(toExecute.get().getLast()) {
            remoteDriverSetup(parentKey, URL);
        }
    }

    /**
     * Launches the specified mobile simulator and navigates to the given URL.
     *
     * @param variableURL The URL variable to use for the mobile simulator
     */
    @Given("^launch the Mobile Simulator \"([^\"]+)\"$")
    public void launchTheMobileSimulator(String variableURL) {
        if(toExecute.get().getLast()) {
            mobileDriverSetup(variableURL);
        }
    }

    /**
     * Navigates the mobile browser to the given URL.
     *
     * @param URL The URL to open
     */
    @And("^navigate mobile browser to \"([^\"]+)\"$")
    public void navigateMobileBrowserTo(String URL) {
        if(toExecute.get().getLast()) {
            navigateToURL(URL);
        }
    }

    /**
     * Clicks an element identified by its name.
     *
     * @param elementName The name of the element to click
     */
    @When("^click element \"([^\"]+)\"$")
    public void clickElement(String elementName) {
        if(toExecute.get().getLast()) {
            clickElement(elementName, null);
        }
    }

    /**
     * Sets text into the specified element.
     *
     * @param value The text to enter
     * @param elementName The target element name
     */
    @When("^set text \"([^\"]+)\" into element \"([^\"]+)\"$")
    public void setTextInto(String value, String elementName) {
        if(toExecute.get().getLast()) {
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
    public void getTextFromAndSetIntoVariable(String elementName, String variableName) {
        if(toExecute.get().getLast()) {
            variables.get().put(variableName, getText(elementName, null));
        }
    }

    /**
     * Waits for an element to become visible within the specified timeout.
     *
     * @param elementName The target element
     * @param timeout Timeout in seconds
     */
    @And("^wait for element \"([^\"]+)\" to be visible(?: within (\\d+) seconds)?$")
    public void waitForElementToBeVisible(String elementName, Integer timeout) {
        if(toExecute.get().getLast()) {
            waitElementVisible(elementName, timeout);
        }
    }

    /**
     * Waits for an element to become exist within the specified timeout.
     *
     * @param elementName The target element
     * @param timeout Timeout in seconds
     */
    @And("^wait for element \"([^\"]+)\" to be exist(?: within (\\d+) seconds)?$")
    public void waitForElementToBeExist(String elementName, Integer timeout) {
        if(toExecute.get().getLast()) {
            waitElementExist(elementName, timeout);
        }
    }

    @And("^click text \"([^\"]+)\"$")
    public void clickText(String text) {
        clickText(text, null);
    }

    @Then("^print \"([^\"]+)\"$")
    public void print(String arg0) {
        if(isSnippet.get()){
            ExtentManager.bufferLog(Status.PASS, currentSnippetStep.get(), takeScreenshot());
        }else {
            ExtentManager.getExtent().pass(currentSnippetStep.get(), takeScreenshot());
        }
    }
}
