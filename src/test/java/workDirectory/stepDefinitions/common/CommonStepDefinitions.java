package workDirectory.stepDefinitions.common;

import builds.actions.BrowserActions;
import builds.actions.MobileActions;
import io.cucumber.java.en.Given;

public class CommonStepDefinitions {

    @Given("I launch the Android Simulator {string}")
    public void launchTheAndroidSimulator(String testName) {
        MobileActions mobileActions = new MobileActions();
        mobileActions.mobileSetup(testName);
    }

    @Given("I launch the browser and navigate to Google page with {string}")
    public void launchTheBrowserAndNavigateToGooglePage(String browserType) {
        BrowserActions browserActions = new BrowserActions();
        browserActions.browserSetup(browserType);
    }
}
