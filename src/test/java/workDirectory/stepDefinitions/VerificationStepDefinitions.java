package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import io.cucumber.java.en.Then;

public class VerificationStepDefinitions extends MainActions {

    /**
     * Verifies if the expected text is equal to the stored variable text.
     *
     * @param expectedText The expected string value
     * @param variableName The name of the variable to compare
     */
    @Then("^verify text \"([^\"]+)\" is equals to variable \"([^\"]+)\"$")
    public void iVerifyTextIsEqualsToVariable(String expectedText, String variableName) {
        if(toExecute.get().getLast()) {
            assertEquals(expectedText, variables.get().get(variableName));
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
        if(toExecute.get().getLast()) {
            verifyElementVisible(elementName, timeout);
        }
    }
}
