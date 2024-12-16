package workDirectory.pages.google;

import builds.actions.BrowserActions;
import workDirectory.pageObject.google.GooglePageObjects;
import workDirectory.stepDefinitions.Hooks;

public class GooglePage extends BrowserActions {
    public void browserSetup(String url){
        browserSetup();
        openURL(url);
    }

    public void typeAndSearch(String text){
       sendKeys(GooglePageObjects.searchBar, text);
       pressEnter();
    }

    public void takeScreenshot(){
        screenshot();
    }
}
