package workDirectory.stepDefinitions;

import builds.actions.MobileActions;
import builds.utilities.BaseTest;
import builds.utilities.GlobalProperties;
import io.cucumber.java.*;
import builds.actions.BrowserActions;

public class Hooks {
    private static Scenario scenario;
    @BeforeStep
    public void setScenario(Scenario scenario) {
        this.scenario=scenario;
    }

    @After
    public static void takeScreenshotIfFailed(Scenario scenario) {
        if (scenario.isFailed()) {
            if (GlobalProperties.getGlobalVariablesProperties().getProperty("currentDriverPlatform").equals("browser")) {
                if (!BaseTest.getWebDriver().toString().contains("null")) {
                    BrowserActions.screenshot();
                }

            } else {
                if (BaseTest.getMobileDriver().getSessionId() != null) {
                    MobileActions.screenshot();
                }
            }
        }
    }

    @AfterStep
    public static void takeScreenshotEveryStep() {
        if (GlobalProperties.getGlobalVariablesProperties().getProperty("screenshot").equals("true")) {
            if (GlobalProperties.getGlobalVariablesProperties().getProperty("currentDriverPlatform").equals("browser")) {
                if (!BaseTest.getWebDriver().toString().contains("null")) {
                    BrowserActions.screenshot();
                }

            } else {
                if (BaseTest.getMobileDriver().getSessionId() != null) {
                    MobileActions.screenshot();
                }
            }
        }
    }

    public static Scenario getScenario(){
        return scenario;
    }

}
