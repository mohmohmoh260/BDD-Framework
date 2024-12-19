package workDirectory.pages.safari;

import builds.actions.MobileActions;
import workDirectory.pageObject.safari.SafariPageObjects;

public class SafariPage {
    private static MobileActions mobileActions = new MobileActions();
    public static void typeSearchBar(String arg0) {
        System.out.println(mobileActions.getMobileDriver());
//        mobileActions.click(SafariPageObjects.searchBar);
//        mobileActions.sendKeys(SafariPageObjects.safariKeyboardSearchBar, arg0);
//        mobileActions.pressEnter();
    }
}
