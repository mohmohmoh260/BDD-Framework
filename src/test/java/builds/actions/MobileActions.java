package builds.actions;

import builds.utilities.BaseTest;
import builds.utilities.GlobalProperties;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import workDirectory.stepDefinitions.Hooks;

import java.time.Duration;
import java.util.List;

public class MobileActions extends BaseTest{

    private static SoftAssert softAssert = new SoftAssert();
    public synchronized void assertElementDisplayed(WebElement element) {
        waitElement(element);
        Assert.assertTrue(element.isDisplayed());
        screenshot();
    }
    
    public synchronized void assertPageTitle(String title) {
        assertPageTitle(title);
    }

    public synchronized static void click(WebElement element) {
        waitElement(element);
        element.click();
    }

    public synchronized static void sendKeys(WebElement element, String input) {
        element.sendKeys(input);
    }

    public synchronized static void close() {
       mobileDriver.close();
    }

    public synchronized static void quit() {
       mobileDriver.quit();
    }

    public synchronized static List<WebElement> findElements(WebElement element){
        return findElements(element);
    }

    public  String getText(WebElement element){
        return getText(element);
    }

//    public synchronized static clear(WebElement element){
//
//    }
//
//    public synchronized static hideKeyboard(){
//        mobileDriver.navigate().back();
//    }

     void scrollDownToElement(WebElement element){
        // ToDo
    }

     void scrollUPToElement(WebElement element){
        // ToDo
    }

    public synchronized static void waitElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(mobileDriver, Duration.ofSeconds(Long.valueOf(GlobalProperties.getGlobalVariablesProperties().getProperty("max.timeout.in.seconds"))));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public synchronized static void screenshot() {
        final byte [] screenshot = ((TakesScreenshot) mobileDriver).getScreenshotAs(OutputType.BYTES);
        Hooks.getScenario().attach(screenshot, "image/png", "");
    }

//    public synchronized static selectDropdownByText(WebElement element, String text){
//
//    }
//
//    public synchronized static selectDropdownByIndex(WebElement element, int index){
//
//    }
//
//    public synchronized static selectDropdownByValue(WebElement element, String text){
//
//    }
//
//    public synchronized static assertPageTitle(WebElement element, String expectedTitle) {
//        String actualTitle = mobileDriver.getTitle();
//        waitElement(element);
//        Assert.assertEquals(actualTitle, expectedTitle);
//    }
//
//    public synchronized static pressEnter() {
//        ((AndroidDriver) mobileDriver).pressKey(new KeyEvent(AndroidKey.ENTER));
//    }
//
//    public synchronized static openURL(String url) {
//        mobileDriver.get(url);
//    }
}
