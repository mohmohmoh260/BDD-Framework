package workDirectory.stepDefinitions.google;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import workDirectory.pages.google.GooglePage;

public class GooglePageStepDefinition {

    @When("send key text {string} into the google search bar and press enter")
    public void sendKeyTextIntoTheGoogleSearchBarAndPressEnter(String text) {
        GooglePage.typeAndSearch(text);
    }

    @Then("assert statistics section is displayed")
    public void assertStatisticsSectionIsDisplayed() {
        GooglePage.assertStatisticSectionDisplayed();

    }

    @Then("assert page title is {string}")
    public void assertPageTitleIs(String text) {
        GooglePage.assertPageTitle(text);
    }
}
