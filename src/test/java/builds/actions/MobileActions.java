package builds.actions;

import builds.utilities.MobileInstance;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

import static workDirectory.stepDefinitions.Hooks.getScenario;

public class MobileActions extends MobileInstance{

    SoftAssert softAssert = new SoftAssert();
    
    public void mobileSetup(String testName){
        mobileInit(testName);
    }

    public String getPlatform(){
        if(isAndroid.get()){
            return "android";
        }else if(isIOS.get()){
            return "ios";
        }else{
            throw new RuntimeException("Check platform properly. Only allowed \"android\" and \"ios\" if current session is mobile");
        }
    }

    public By fetchElement(String elementName){
        return By.xpath(getElementValue(elementName, getPlatform()));
    }

    public List<WebElement> fetchElements(String elementName){
        return appiumDriver.get().findElements(By.xpath(getElementValue(elementName, getPlatform())));
    }

    public void assertElementDisplayed(String elementName) {
        waitElement(elementName);
        softAssert.assertTrue(appiumDriver.get().findElement(fetchElement(elementName)).isDisplayed());
        screenshot();
    }
    
    public void assertPageTitle(String title) {
        softAssert.assertEquals(title, appiumDriver.get().getTitle());
    }

    public void click(String elementName) {
        waitElement(elementName);
        appiumDriver.get().findElement(fetchElement(elementName)).click();
    }

    public void setText(String input, String elementName) {
        waitElement(elementName);
        appiumDriver.get().findElement(fetchElement(elementName)).clear();
        appiumDriver.get().findElement(fetchElement(elementName)).sendKeys(input);
    }

    public void close() {
        appiumDriver.get().close();
    }

    public void quit() {
        appiumDriver.get().quit();
    }

    public  List<WebElement> findElements(String elementName){
        return fetchElements(elementName);
    }

    public String getText(String elementName){
        waitElement(elementName);
        return appiumDriver.get().findElement(fetchElement(elementName)).getText();
    }

    public void clear(String elementName){
        appiumDriver.get().findElement(fetchElement(elementName)).clear();
    }

    public void hideKeyboard(){

    }

     void scrollDownToElement(WebElement element){
        // ToDo
    }

     void scrollUPToElement(WebElement element){
        // ToDo
    }

    public boolean waitElement(String elementName) {
//        By by = null;
//        String type = element.toString();
//        type = type.replace("Located by By.chained({AppiumBy.","");
//        type = type.replace("Located by By.chained({By.","");
//        String[] getType = type.split(":");
//        type = getType[0].trim();
//        String xpath = getType[1].replace("})","").trim();
//        if(type.equals("xpath")){
//            by = By.xpath(xpath);
//        }else if(type.equals("id")){
//            by = By.id(xpath);
//        }else if(type.equals("css")){
//            by = By.cssSelector(xpath);
//        }else if(type.equals("className")){
//            by = By.className(xpath);
//        }else if(type.equals("linkText")){
//            by = By.linkText(xpath);
//        }else if(type.equals("name")){
//            by = By.name(xpath);
//        }else if(type.equals("partialLinkText")){
//            by = By.partialLinkText(xpath);
//        }else if(type.equals("tagName")){
//            by = By.tagName(xpath);
//        }else{
//            System.err.println("Caught Exception: Check the WebElement to be split properly: "+ element);
//        }
        try{
            WebDriverWait wait = new WebDriverWait(appiumDriver.get(), Duration.ofSeconds(Long.parseLong(globalDeviceParameter.get(0).get("timeOut"))));
            wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
            wait.until((ExpectedConditions.visibilityOf(appiumDriver.get().findElement(fetchElement(elementName)))));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void screenshot() {
        try {
            TakesScreenshot screenshotDriver = (TakesScreenshot) appiumDriver.get();
            byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            getScenario().attach(screenshot, "image/png", "Screenshot");
        } catch (Exception e) {
            getScenario().log("Screenshot failed: " + e.getMessage());
        }
    }

    public void pressEnter() {
        if(appiumDriver.get().toString().contains("IOSDriver")){
            appiumDriver.get().findElement(By.xpath("//XCUIElementTypeButton[@name=\"Go\"]")).click();
        }else{
            ((AndroidDriver) appiumDriver.get()).pressKey(new KeyEvent(AndroidKey.ENTER));
        }
    }

    public boolean verifyElementVisible(String elementName) {
        try{
            softAssert.assertTrue(waitElement(elementName), "Element "+elementName+" with locator: "+getElementValue(elementName, getPlatform()));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void navigateToURL(String URL) {
        appiumDriver.get().get(globalDeviceParameter.get(0).get(URL));
    }
}
