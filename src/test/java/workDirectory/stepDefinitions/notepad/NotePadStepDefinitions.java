package workDirectory.stepDefinitions.notepad;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import workDirectory.pages.notepad.NotePadAppPage;
import workDirectory.stepDefinitions.Hooks;
import workDirectory.stepDefinitions.common.CommonStepDefinitions;

public class NotePadStepDefinitions {
    NotePadAppPage notePadAppPage = new NotePadAppPage();

    CommonStepDefinitions commonStepDefinitions = new CommonStepDefinitions();
    @Then("print driver instance ID")
    public void printDriverInstanceID() {
        notePadAppPage.printDriverInstanceID();
    }

    @Then("take screenshot")
    public void takeScreenshot() {
        commonStepDefinitions.iTakeBrowserScreenshot();
    }
}
