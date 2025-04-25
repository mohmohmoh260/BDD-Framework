package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import builds.extent.ExtentManager;
import builds.listener.StepListener;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.cucumber.java.BeforeStep;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestStep;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.java.Scenario;

import java.lang.reflect.Field;
import java.util.List;

public class Hooks extends MainActions {

    private static final ThreadLocal<Integer> stepCounter = ThreadLocal.withInitial(() -> 0);

    @Before
    public void beforeScenario(Scenario scenario){
        ExtentManager.getInstance().setSystemInfo("OS", System.getProperty("os.name"));
        ExtentTest extentTest = ExtentManager.getInstance().createTest(scenario.getName());
        ExtentManager.setExtent(extentTest);
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        if(!toExecute.get().getLast()){
            ExtentManager.bufferLog(Status.SKIP, "⏭ Skipping test: " + StepListener.gherkinStep.get(), null);
        }
        int currentStepIndex = stepCounter.get();
        String stepText = getStepByIndex(scenario, currentStepIndex);
        stepCounter.set(currentStepIndex + 1);
        ExtentManager.createAndPushNode(stepText);
    }

    @AfterStep
    public void afterStep(Scenario scenario){
        if(!isSnippet.get()){
            if(scenario.isFailed()){
                Throwable error = StepListener.lastStepError.get();
                ExtentManager.bufferLog(Status.FAIL, "<span style='color:red'>" + StepListener.gherkinStep.get() + "</span><br><br>" + error, takeScreenshot());
            }else {
                ExtentManager.bufferLog(Status.PASS, StepListener.gherkinStep.get(),  takeScreenshot());
            }
        }
        isSnippet.set(false);
        ExtentManager.popNode();
    }

    @After
    public void afterScenario() {
        if(driver.get() instanceof AppiumDriver){
            ExtentManager.getExtent().assignDevice(
                    "<b>Platform Name:&nbsp;</b>" + ((AppiumDriver) driver.get()).getCapabilities().getPlatformName() + "<br>" +
                            "<b>Platform Version:&nbsp;</b>" + ((AppiumDriver) driver.get()).getCapabilities().getCapability("platformVersion").toString() + "<br>" +
                            "<b>Device Name:&nbsp;</b>" + ((AppiumDriver) driver.get()).getCapabilities().getCapability("deviceName").toString()
            );
        }else if(driver.get() instanceof RemoteWebDriver){
            ExtentManager.getExtent().assignDevice(
                    "<b>Browser Name:&nbsp;</b>" + ((RemoteWebDriver) driver.get()).getCapabilities().getBrowserName() + "<br>" +
                            "<b>Version:&nbsp;</b>" + ((RemoteWebDriver) driver.get()).getCapabilities().getBrowserVersion()
            );
        }else{
           return;
        }
        ExtentManager.flush();
    }

    private String getStepByIndex(Scenario scenario, int index) {
        try {
            Field delegateField = scenario.getClass().getDeclaredField("delegate");
            delegateField.setAccessible(true);
            Object scenarioImpl = delegateField.get(scenario);

            Field testCaseField = scenarioImpl.getClass().getDeclaredField("testCase");
            testCaseField.setAccessible(true);
            TestCase testCase = (TestCase) testCaseField.get(scenarioImpl);

            List<TestStep> steps = testCase.getTestSteps();
            int stepCount = 0;
            for (TestStep step : steps) {
                if (step instanceof PickleStepTestStep) {
                    if (stepCount == index) {
                        return ((PickleStepTestStep) step).getStep().getText();
                    }
                    stepCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
