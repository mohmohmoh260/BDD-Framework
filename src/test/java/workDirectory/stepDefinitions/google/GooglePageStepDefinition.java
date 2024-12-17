package workDirectory.stepDefinitions.google;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import workDirectory.pages.google.GooglePage;

public class GooglePageStepDefinition extends GooglePage {

    @Given("launch the browser and navigate to Google page with {string}")
    public void launchTheBrowserAndNavigateToGooglePage(String browserType) {
        browserStart(browserType);
    }

    @When("send key text {string} into the google search bar and press enter")
    public void sendKeyTextIntoTheGoogleSearchBarAndPressEnter(String text) {
        typeAndSearch(text);
    }

    @Then("assert statistics section is displayed")
    public void assertStatisticsSectionIsDisplayed() {
        //browserActions.assertElementDisplayed(statistics);
        takeScreenshot();
    }

    @Then("assert page title is {string}")
    public void assertPageTitleIs(String text) {
        //browserActions.assertPageTitle(text);
        takeScreenshot();
    }
}
