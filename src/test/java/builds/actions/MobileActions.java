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

import static workDirectory.stepDefinitions.Hooks.getScenario;

public class MobileActions extends MobileInstance{
    TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
    private SoftAssert softAssert = new SoftAssert();

    public void mobileSetup(String testName){
        mobileInit(testName);
    }

    public By getBy(WebElement element){
        System.out.println(element);
        By by = null;
        String type = element.toString();
        type = type.replace("Located by By.chained({AppiumBy.","");
        type = type.replace("Located by By.chained({By.","");
        String[] getType = type.split(":");
        type = getType[0].trim();
        String xpath = getType[1].replace("})","").trim();
        System.out.println("id: "+type);
        System.out.println("xpath: "+xpath);
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

    public void sendKeys(WebElement element, String input) {
        getMobileDriver().findElement(getBy(element)).sendKeys(input);
    }

    public void close() {
        getMobileDriver().close();
    }

    public void quit() {
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
        WebDriverWait wait = new WebDriverWait(getMobileDriver(), Duration.ofSeconds(Long.valueOf(testNGXmlParser.getGlobalParameters().get(0).get("timeOut"))));
        wait.until(ExpectedConditions.visibilityOf(getMobileDriver().findElement(getBy(element))));
    }

    public void screenshot() {
        byte[] screenshot = ((TakesScreenshot) getMobileDriver()).getScreenshotAs(OutputType.BYTES);
        getScenario().attach(screenshot, "image/png", "Screenshot");
    }

    public void pressEnter() {
        if(getMobileDriver().toString().contains("IOSDriver")){
            getMobileDriver().findElement(By.xpath("//XCUIElementTypeButton[@name=\"Go\"]")).click();
        }else{
            ((AndroidDriver) getMobileDriver()).pressKey(new KeyEvent(AndroidKey.ENTER));
        }

    }

}
