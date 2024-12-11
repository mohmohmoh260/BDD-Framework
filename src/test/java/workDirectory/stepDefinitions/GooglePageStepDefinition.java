package workDirectory.stepDefinitions;

import builds.actions.BrowserActions;
import builds.utilities.GlobalProperties;
import workDirectory.pages.google.GooglePage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GooglePageStepDefinition extends GooglePage{

    @Given("launch the browser and navigate to Google page")
    public void launchTheBrowserAndNavigateToGooglePage() {
        BrowserActions.browserSetup();
        BrowserActions.openURL("browser.google.url");
    }

    @When("send key text {string} into the google search bar and press enter")
    public void sendKeyTextIntoTheGoogleSearchBarAndPressEnter(String text) {
        BrowserActions.sendKeys(GooglePage.searchBar, text);
        BrowserActions.pressEnter();
    }

    @Then("assert statistics section is displayed")
    public void assertStatisticsSectionIsDisplayed() {
        BrowserActions.assertElementDisplayed(GooglePage.statistics);
    }

    @Then("assert page title is {string}")
    public void assertPageTitleIs(String text) {
        BrowserActions.assertPageTitle(text);
    }
}
