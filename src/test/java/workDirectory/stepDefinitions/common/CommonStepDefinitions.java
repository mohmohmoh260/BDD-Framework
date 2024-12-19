package workDirectory.stepDefinitions.common;

import builds.actions.BrowserActions;
import builds.actions.MobileActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class CommonStepDefinitions {
    MobileActions mobileActions = new MobileActions();
    BrowserActions browserActions = new BrowserActions();

    @Given("I launch the Mobile Simulator {string}")
    public void launchTheMobileSimulator(String testName) {
        mobileActions.mobileSetup(testName);
    }

    @Given("I launch the browser and navigate to Google page with {string}")
    public void launchTheBrowserAndNavigateToGooglePage(String browserType) {
        browserActions.browserSetup(browserType);
    }

    @And("I take browser screenshot")
    public void iTakeBrowserScreenshot(){
        browserActions.screenshot();
    }

    @And("I take mobile screenshot")
    public void iTakeMobileScreenshot(){
        mobileActions.screenshot();
    }
}
