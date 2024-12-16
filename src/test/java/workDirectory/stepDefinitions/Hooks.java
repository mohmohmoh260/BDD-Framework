package workDirectory.stepDefinitions;

import builds.actions.MobileActions;
import builds.utilities.BrowserInstance;
import io.cucumber.java.*;
import builds.actions.BrowserActions;

public class Hooks{
    // Use ThreadLocal to store Scenario for each thread
    private static final ThreadLocal<Scenario> threadLocalScenario = new ThreadLocal<>();

    @Before
    public void setScenario(Scenario scenario) {
        threadLocalScenario.set(scenario);
    }

    public static Scenario getScenario() {
        return threadLocalScenario.get();
    }

    @AfterStep
    public void takeScreenshotIfFailed(Scenario scenario) {
        if (scenario.isFailed()) {
            MobileActions mobileActions = new MobileActions();
            BrowserInstance browserInstance = new BrowserInstance();
            BrowserActions browserActions = new BrowserActions();
            if (browserInstance.getWebDriver()!=null) {
                browserActions.screenshot();
            } else {
                mobileActions.screenshot();
            }
        }
    }
}
