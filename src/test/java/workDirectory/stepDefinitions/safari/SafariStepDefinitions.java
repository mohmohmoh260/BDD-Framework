package workDirectory.stepDefinitions.safari;

import io.cucumber.java.en.When;
import workDirectory.pages.safari.SafariPage;

public class SafariStepDefinitions {
    @When("I type {string} into searchbar")
    public void iTypeIntoSearchbar(String arg0) {
        SafariPage.typeSearchBar(arg0);
    }

}
