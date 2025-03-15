package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import builds.utilities.StepListener;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class Hooks extends MainActions {

    private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        currentScenario.set(scenario);
        if(!toExecute.get()){
            currentScenario.get().log("⏭ Skipping test: "+ StepListener.gherkinStep.get());
        }
    }

    public static Scenario getScenario() {
        return currentScenario.get();
    }

    @AfterStep
    public void takeScreenshotIfFailed(Scenario scenario) {
        if(globalDeviceParameter.get().get(0).get("screenshotEveryStep").equals("true")){
            screenshot();
        }
    }
}
