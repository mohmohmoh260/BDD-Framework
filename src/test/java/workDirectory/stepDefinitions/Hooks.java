package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import builds.extent.ExtentManager;
import builds.listener.StepListener;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.*;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Hooks extends MainActions {

    private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

    @Before
    public void beforeScenario(Scenario scenario){
        ExtentManager.getInstance().setSystemInfo("OS", System.getProperty("os.name"));
        ExtentTest extentTest = ExtentManager.getInstance().createTest(scenario.getName());
        ExtentManager.setExtent(extentTest);
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        currentScenario.set(scenario);
        if(!toExecute.get()){
            currentScenario.get().log("⏭ Skipping test: "+ StepListener.gherkinStep.get());
        }
    }

    public Scenario getScenario() {
        return currentScenario.get();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if(!isSnippet.get()){
            if (scenario.isFailed()) {
                ExtentManager.getExtent().fail(StepListener.gherkinStep.get() + "<br><br>", takeScreenshot());
            } else {
                if(globalDeviceParameter.get().get(0).get("screenshotEveryStep").equals("true")||StepListener.gherkinStep.get().equals("And take screenshot")){
                    ExtentManager.getExtent().pass(StepListener.gherkinStep.get() , takeScreenshot());
                }else {
                    ExtentManager.getExtent().pass(StepListener.gherkinStep.get());
                }
            }
        }
        isSnippet.set(false);
    }

    @After
    public void afterScenario() {
        if(driver.get() instanceof AppiumDriver){
            ExtentManager.getExtent().assignDevice(
                    "<b>Platform Name:</b>&nbsp;" + ((AppiumDriver) driver.get()).getCapabilities().getPlatformName() + "<br>" +
                            "<b>Platform Version:</b>&nbsp;" + ((AppiumDriver) driver.get()).getCapabilities().getCapability("platformVersion").toString() + "<br>" +
                            "<b>Device Name:</b>&nbsp;" + ((AppiumDriver) driver.get()).getCapabilities().getCapability("deviceName").toString()
            );
        }else if(driver.get() instanceof RemoteWebDriver){
            ExtentManager.getExtent().assignDevice(
                    "<b>Browser Name:</b>&nbsp;" + ((RemoteWebDriver) driver.get()).getCapabilities().getBrowserName() + "<br>" +
                            "<b>Version:</b>&nbsp;" + ((RemoteWebDriver) driver.get()).getCapabilities().getBrowserVersion()
            );
        }else{
           return;
        }
        ExtentManager.flush();
    }
}
