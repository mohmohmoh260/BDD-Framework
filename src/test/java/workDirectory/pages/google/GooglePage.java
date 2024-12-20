package workDirectory.pages.google;

import builds.actions.BrowserActions;
import workDirectory.pageObject.google.GooglePageObjects;

public class GooglePage extends BrowserActions{

    public void typeAndSearch(String text){
        sendKeys(GooglePageObjects.searchBar, text);
        pressEnter();
    }

    public void assertStatisticSectionDisplayed() {
    }

    public void assertPageTitle(String title) {
        assertPageTitle(title);
    }
}
