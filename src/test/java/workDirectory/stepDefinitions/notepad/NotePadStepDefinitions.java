package workDirectory.stepDefinitions.notepad;

import builds.actions.MobileActions;
import org.openqa.selenium.WebElement;
import workDirectory.pageObject.notepad.NotePadAppsPageObjects;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import workDirectory.pages.notepad.NotePadAppPage;

import java.util.List;
import java.util.Map;

public class NotePadStepDefinitions extends NotePadAppPage {
    
    @Given("^launch the mobile apps and open notepad$")
    public void NotePadStepDefinitions() {
        androidSetup();
    }

    @And("create new note")
    public void createNewNote() {
        clickNewNote();
    }

    @And("type {string} note title")
    public void typeNoteTitle(String text) {
        typeTitle(text);
    }

    @When("type {string} note body")
    public void typeNoteBody(String text) {
        typeIntoBody(text);
    }
//    @And("type note title")
//    public void typeNoteTitle(List<List<String>>  list) {
//        for(List<String> l : list){
//            MobileActions.sendKeys(NotePadAppsPageObjects.noteTitleInput, l.get(0)+" "+l.get(1)+" "+l.get(2));
//        }
//
//    }
//
//    @And("type note body")
//    public void typeNoteBody(List<Map<String,String>> map) {
//        for(Map<String,String> list : map){
//            MobileActions.sendKeys(NotePadAppsPageObjects.noteBodyInput, list.get("First Word")+" "+list.get("Second Word"));
//        }
//
//    }
}
