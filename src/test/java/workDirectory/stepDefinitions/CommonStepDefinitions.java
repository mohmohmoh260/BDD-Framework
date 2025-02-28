package workDirectory.stepDefinitions;

import builds.main.CucumberRun;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.Pickle;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.DataProvider;

import java.util.Arrays;

public class CommonStepDefinitions extends CommonMethods{

//    @Given("I launch the Mobile Simulator {string}")
//    public void launchTheMobileSimulator(String testName) {
//        mobileSetup(testName);
//    }
//
//    @Given("I launch the browser and navigate to Google page with {string}")
//    public void launchTheBrowserAndNavigateToGooglePage(String browserType) {
//        browserSetup(browserType);
//    }
//
//    @And("I take browser screenshot")
//    public void iTakeBrowserScreenshot(){
//        browserScreenshot();
//    }
//
//    @And("I take mobile screenshot")
//    public void iTakeMobileScreenshot(){
//        mobileScreenshot();
//    }

    @And ("if {int} is bigger than {int}")
    public void ifNumberIsBiggerThanNumber(int firstValue, int secondValue){
       addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Arrays.asList(firstValue,secondValue));
    }

    @And ("if {int} is smaller than {int}")
    public void ifNumberIsSmallerThanNumber(int firstValue, int secondValue){
        addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Arrays.asList(firstValue,secondValue));
    }

    @And ("if {string} is not visible")
    public void ifElementIsNotVisible(String elementName){
        addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Arrays.asList(elementName));
    }

    @And("end statement")
    public void endIfStatement(){
        endIf();
    }


    @And("I launch the browser and navigate to Google page with {string}")
    public void i_launch_the_browser_and_navigate_to_google_page_with(String string) {
        System.out.println(string);
    }

    int i=0;
    @Then("print driver instance ID")
    public void printDriverInstanceID() {
        System.out.println(i);
        i++;
    }


    @When("run snippet scenario {string}")
    public void runSnippetScenario(String arg0) {
        CucumberRun.TestRunner x = new CucumberRun.TestRunner();
        x.runScenarioByName(arg0);
    }
}
