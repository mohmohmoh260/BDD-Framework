package workDirectory.stepDefinitions;

import builds.utilities.BrowserInstance;
import builds.utilities.MobileInstance;
import builds.utilities.StepListener;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class Hooks extends CommonMethods {
    private static final ThreadLocal<Scenario> threadLocalScenario = new ThreadLocal<>();

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        threadLocalScenario.set(scenario);
        if(!toExecute.get()){
            System.out.println("Skipping test: "+ StepListener.gherkinStep.get());
        }
    }

    public static Scenario getScenario() {
        return threadLocalScenario.get();
    }

    @AfterStep
    public void takeScreenshotIfFailed(Scenario scenario) {
        if (scenario.isFailed()) {
            BrowserInstance browserInstance = new BrowserInstance();
            MobileInstance mobileInstance = new MobileInstance();
            if (browserInstance.getWebDriver()!=null) {
                browserActions.screenshot();
            } else if(mobileInstance.getMobileDriver()!=null){
                mobileActions.screenshot();
            }
        }
    }
}
