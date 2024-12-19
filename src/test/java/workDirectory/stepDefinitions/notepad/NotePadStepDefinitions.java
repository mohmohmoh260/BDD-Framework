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

}
