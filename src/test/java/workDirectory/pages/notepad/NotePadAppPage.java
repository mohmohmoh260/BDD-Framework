package workDirectory.pages.notepad;

import builds.actions.MobileActions;
import workDirectory.pageObject.notepad.NotePadAppsPageObjects;
import workDirectory.stepDefinitions.Hooks;

public class NotePadAppPage extends MobileActions {

    protected void clickNewNote(){
        click(NotePadAppsPageObjects.newNoteButton);
    }

    protected void typeTitle(String text){
        sendKeys(NotePadAppsPageObjects.noteTitleInput, text);
    }

    protected void typeIntoBody(String text){
        sendKeys( NotePadAppsPageObjects.noteBodyInput, text);
    }

    public void printDriverInstanceID() {
        Hooks.getScenario().log(String.valueOf(getMobileDriver().getSessionId()));
        screenshot();
    }
}
