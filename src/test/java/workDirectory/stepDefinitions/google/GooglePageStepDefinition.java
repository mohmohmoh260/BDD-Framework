package workDirectory.stepDefinitions.google;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import workDirectory.pages.google.GooglePage;

public class GooglePageStepDefinition {

    GooglePage googlePage = new GooglePage();

    @When("send key text {string} into the google search bar and press enter")
    public void sendKeyTextIntoTheGoogleSearchBarAndPressEnter(String text) {
        googlePage.typeAndSearch(text);
    }

    @Then("assert statistics section is displayed")
    public void assertStatisticsSectionIsDisplayed() {
        googlePage.assertStatisticSectionDisplayed();

    }

    @Then("assert page title is {string}")
    public void assertPageTitleIs(String text) {
        googlePage.assertPageTitle(text);
    }
}
