package workDirectory.stepDefinitions.notepad;

import builds.actions.MobileActions;
import builds.utilities.TestNGXmlParser;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import workDirectory.pages.notepad.NotePadAppPage;

public class NotePadStepDefinitions extends NotePadAppPage {

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

    @Given("launch the iOS Simulator")
    public void launchTheIOSSimulator() {

    }

    @Given("launch the Android Simulator {string}")
    public void launchTheAndroidSimulator(String testName) {
        start(testName);
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
