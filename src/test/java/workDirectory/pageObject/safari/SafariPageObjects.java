package workDirectory.pageObject.safari;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class SafariPageObjects {

    @AndroidFindBy(id = "")
    @iOSXCUITFindBy(id = "TabBarItemTitle")
    public static WebElement searchBar;

    @AndroidFindBy(id = "")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeApplication[@name=\"Safari\"]/XCUIElementTypeWindow[3]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]")
    public static WebElement safariKeyboardSearchBar;

}
