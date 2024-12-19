package workDirectory.pages.safari;

import builds.actions.MobileActions;
import workDirectory.pageObject.safari.SafariPageObjects;

public class SafariPage extends MobileActions {

    protected void typeSearchBar(String arg0) {
        click(SafariPageObjects.searchBar);
        sendKeys(SafariPageObjects.searchBar, arg0);
    }
}
