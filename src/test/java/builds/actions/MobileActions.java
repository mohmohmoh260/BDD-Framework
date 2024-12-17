package builds.actions;

import builds.utilities.MobileInstance;
import builds.utilities.TestNGXmlParser;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static workDirectory.stepDefinitions.Hooks.getScenario;

public class MobileActions extends MobileInstance{
    private SoftAssert softAssert = new SoftAssert();

    public void mobileSetup(String testName){
        TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
        List<Map<String, String>> filteredList = testNGXmlParser.filterXMLByTestName(testName);
        mobileInit(filteredList.get(0).get("platformName"), filteredList.get(0).get("Global_appiumServerIP"), filteredList.get(0).get("port"), filteredList.get(0).get("udid"), filteredList.get(0).get("automationName"), filteredList.get(0).get("appPackage"), filteredList.get(0).get("appActivity"), filteredList.get(0).get("fullReset"), filteredList.get(0).get("noReset"), filteredList.get(0).get("apkPath"), filteredList.get(0).get("bundleID"), filteredList.get(0).get("deviceName"), filteredList.get(0).get("platformVersion"));
    }

    public By getBy(WebElement element){
        By by = null;
        String type = element.toString();
        String[] split1 = type.split("\\.");
        String[] split2 = type.split(":");
        String[] split3 = split1[1].split(":");
        String xpath = split2[1].trim();
        type = split3[0].trim();
        if(type.equals("xpath")){
            by = By.xpath(xpath);
        }else if(type.equals("id")){
            by = By.id(xpath);
        }else if(type.equals("css")){
            by = By.cssSelector(xpath);
        }else if(type.equals("className")){
            by = By.className(xpath);
        }else if(type.equals("linkText")){
            by = By.linkText(xpath);
        }else if(type.equals("name")){
            by = By.name(xpath);
        }else if(type.equals("partialLinkText")){
            by = By.partialLinkText(xpath);
        }else if(type.equals("tagName")){
            by = By.tagName(xpath);
        }else{
            System.err.println("Caught Exception: Check the xpath type in page object");
        }
        return by;
    }

    public void assertElementDisplayed(WebElement element) {
        waitElement(element);
        Assert.assertTrue(getMobileDriver().findElement(getBy(element)).isDisplayed());
        screenshot();
    }
    
    public void assertPageTitle(String title) {
        softAssert.assertTrue(getMobileDriver().getTitle().equals(title));
    }

    public void click(WebElement element) {
        waitElement(element);
        getMobileDriver().findElement(getBy(element)).click();
    }

    public  void sendKeys(WebElement element, String input) {
        getMobileDriver().findElement(getBy(element)).sendKeys(input);
    }

    public  void close() {
        getMobileDriver().close();
    }

    public  void quit() {
        getMobileDriver().quit();
    }

    public  List<WebElement> findElements(WebElement element){
        return getMobileDriver().findElements(getBy(element));
    }

    public String getText(WebElement element){
        return getMobileDriver().findElement(getBy(element)).getText();
    }

//    public  clear(WebElement element){
//
//    }

//    public  hideKeyboard(){
//        MobileInstance.getMobileDriver();
//    }

     void scrollDownToElement(WebElement element){
        // ToDo
    }

     void scrollUPToElement(WebElement element){
        // ToDo
    }

    public void waitElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getMobileDriver(), Duration.ofSeconds(Long.valueOf("30")));
        wait.until(ExpectedConditions.visibilityOf(getMobileDriver().findElement(getBy(element))));
    }

    public void screenshot() {
        byte[] screenshot = ((TakesScreenshot) getMobileDriver()).getScreenshotAs(OutputType.BYTES);
        getScenario().attach(screenshot, "image/png", "Screenshot");
    }

    public  void pressEnter() {
        ((AndroidDriver) getMobileDriver()).pressKey(new KeyEvent(AndroidKey.ENTER));
    }

}
