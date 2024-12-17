package workDirectory.pages.notepad;

import builds.actions.MobileActions;
import workDirectory.pageObject.notepad.NotePadAppsPageObjects;

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

    public void start(String text){
        MobileActions mobileActions = new MobileActions();
        mobileActions.mobileSetup(text);
    }

}
