package builds.actions;

import builds.driver.MobileInstance;
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
        setupMobileDriver(testName);
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

    public List<WebElement> findElements(String elementName, Integer timeout){
        waitElementExist(elementName, timeout);
        return appiumDriver.get().findElements(By.xpath(getElementValue(elementName, getPlatform())));
    }

    public void assertElementDisplayed(String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        softAssert.assertTrue(appiumDriver.get().findElement(fetchElement(elementName)).isDisplayed());
        screenshot();
    }
    
    public void assertPageTitle(String title) {
        softAssert.assertEquals(title, appiumDriver.get().getTitle());
    }

    public void click(String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        appiumDriver.get().findElement(fetchElement(elementName)).click();
    }

    public void setText(String input, String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        appiumDriver.get().findElement(fetchElement(elementName)).clear();
        appiumDriver.get().findElement(fetchElement(elementName)).sendKeys(input);
    }

    public void close() {
        appiumDriver.get().close();
    }

    public void quit() {
        appiumDriver.get().quit();
    }

    public String getText(String elementName, Integer timeout){
        waitElementVisible(elementName, timeout);
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

    public boolean waitElementExist(String elementName, Integer timeout) {
        try{
            if (timeout == null) {
                timeout = Integer.parseInt(globalDeviceParameter.get(0).get("timeOut"));
            }
            WebDriverWait wait = new WebDriverWait(appiumDriver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
            wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean waitElementVisible(String elementName, Integer timeout){
        try{
            if (timeout == null) {
                timeout = Integer.parseInt(globalDeviceParameter.get(0).get("timeOut"));
            }
            WebDriverWait wait = new WebDriverWait(appiumDriver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
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

    public boolean verifyElementVisible(String elementName, Integer timeout) {
        try{
            softAssert.assertTrue(waitElementVisible(elementName, timeout), "Element "+elementName+" with locator: "+getElementValue(elementName, getPlatform()));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void navigateToURL(String URL) {
        appiumDriver.get().get(globalDeviceParameter.get(0).get(URL));
    }
}
