package workDirectory.stepDefinitions.safari;

import io.cucumber.java.en.When;
import workDirectory.pages.safari.SafariPage;

public class SafariStepDefinitions extends SafariPage {
    @When("I type {string} into searchbar")
    public void iTypeIntoSearchbar(String arg0) {
        typeSearchBar(arg0);
    }

}
