package workDirectory.pages.google;

import builds.actions.BrowserActions;
import workDirectory.pageObject.google.GooglePageObjects;

public class GooglePage extends BrowserActions {
    public void browserStart(String browserType){
        browserSetup(browserType);
    }

    public void typeAndSearch(String text){
       sendKeys(GooglePageObjects.searchBar, text);
       pressEnter();
    }

    public void takeScreenshot(){
        screenshot();
    }
}
