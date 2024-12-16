package workDirectory.pageObject.google;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GooglePageObjects {

    @FindBy(xpath = "//textarea[@title='Search']")
    public static WebElement searchBar;
    @FindBy(xpath = "//h3[text()=\"COVIDNOW in Malaysia - COVIDNOW\"]")
    public static WebElement statistics;

}
