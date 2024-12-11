package workDirectory.pages.google;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import builds.actions.BrowserActions;

public class GooglePage {

    @FindBy(xpath = "//textarea[@title='Search']")
    public static WebElement searchBar;
    @FindBy(xpath = "//h3[text()=\"COVIDNOW in Malaysia - COVIDNOW\"]")
    public static WebElement statistics;

}
