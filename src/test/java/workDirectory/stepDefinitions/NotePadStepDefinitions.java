package workDirectory.stepDefinitions;

import builds.actions.MobileActions;
import builds.utilities.BaseTest;
import builds.utilities.Context;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import workDirectory.pages.notepad.NotePadApps;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static workDirectory.pages.notepad.NotePadApps.noteTitleInput;

public class NotePadStepDefinitions {
    Context context;
    public NotePadStepDefinitions(Context context){
        this.context = context;
    }
    
    @Given("^launch the mobile apps and open notepad$")
    public void NotePadStepDefinitions() {
        BaseTest.androidSetup();
    }

    @And("create new note")
    public void createNewNote() {
        MobileActions.click(NotePadApps.newNoteButton);
    }

    @And("type {string} note title")
    public void typeNoteTitle(String text) {
        MobileActions.sendKeys(NotePadApps.noteTitleInput, text);
    }

    @When("type {string} note body")
    public void typeNoteBody(String text) {
        MobileActions.sendKeys(NotePadApps.noteBodyInput, text);
    }

    @Then("save the note")
    public void saveTheNote() {
        MobileActions.click(NotePadApps.saveButton);
    }

    @When("click back button")
    public void clickBackButton() {
        MobileActions.click(NotePadApps.backButton);
    }

    @Then("assert note {string} is displayed")
    public void assertNoteIsDisplayed(String text) {
        for(WebElement e : NotePadApps.listNoteTitle){
            if(e.getText().contains(text)){
                context.setContext("note", e.getText());
                MobileActions.screenshot();
                break;
            }
        }
    }

    @And("Quit")
    public void quit() {
        MobileActions.quit();
    }

    @And("type note title")
    public void typeNoteTitle(List<List<String>>  list) {
        for(List<String> l : list){
            MobileActions.sendKeys(NotePadApps.noteTitleInput, l.get(0)+" "+l.get(1)+" "+l.get(2));
        }

    }

    @And("type note body")
    public void typeNoteBody(List<Map<String,String>> map) {
        for(Map<String,String> list : map){
            MobileActions.sendKeys(NotePadApps.noteBodyInput, list.get("First Word")+" "+list.get("Second Word"));
        }

    }

    @Given("launch the iOS Simulator")
    public void launchTheIOSSimulator() {
        BaseTest.iOSSetup();
    }


    @Then("click Allow Access")
    public void clickAllowAccess() {
        MobileActions.click(NotePadApps.noteTitleInput);
        MobileActions.click(NotePadApps.newNoteButton);
    }
}
