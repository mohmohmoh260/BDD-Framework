package workDirectory.pages.google;

import builds.actions.BrowserActions;
import workDirectory.pageObject.google.GooglePageObjects;

public class GooglePage {
    private static BrowserActions browserActions = new BrowserActions();

    public static void typeAndSearch(String text){
        browserActions.sendKeys(GooglePageObjects.searchBar, text);
        browserActions. pressEnter();
    }

    public static void assertStatisticSectionDisplayed() {
    }

    public static void assertPageTitle(String title) {
        browserActions.assertPageTitle(title);
    }
}
