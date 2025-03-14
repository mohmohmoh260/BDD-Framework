package builds.actions;

import builds.driver.BrowserDriver;
import builds.driver.MainDriver;
import builds.utilities.TestNGXmlParser;
import builds.driver.MobileDriver;
import builds.driver.RemoteDriver;
import builds.elements.ElementInstance;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.*;
import org.testng.Assert;
import workDirectory.stepDefinitions.Hooks;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static workDirectory.stepDefinitions.Hooks.getScenario;

public abstract class MainActions extends MainDriver {

    protected static final List<Map<String,String>> globalDeviceParameter = TestNGXmlParser.getGlobalParameters();
    protected static final ThreadLocal<Boolean> toExecute = ThreadLocal.withInitial(() -> true);
    protected static final ThreadLocal<HashMap<String, String>> variables = ThreadLocal.withInitial(HashMap::new);
    private static final ElementInstance elementInstance = new ElementInstance();

    public static class Result { // Make it public static
        public boolean success;
        String message;

        public Result(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    public boolean waitElementExist(String elementName, Integer timeout) {
        try{
            if (timeout == null) {
                timeout = Integer.parseInt(globalDeviceParameter.get(0).get("timeOut"));
            }
            WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
            wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
            return true;
        }catch (TimeoutException e){
            return false;
        }
    }

    public boolean waitElementVisible(String elementName, Integer timeout){
        try{
            if (timeout == null) {
                timeout = Integer.parseInt(globalDeviceParameter.get(0).get("timeOut"));
            }
            WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
            wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
            wait.until((ExpectedConditions.visibilityOf(driver.get().findElement(fetchElement(elementName)))));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void browserDriverSetup(String browserType, String URL){
        BrowserDriver browserDriver = new BrowserDriver();
        browserDriver.setupBrowserDriver(browserType, URL);
    }

    public void mobileDriverSetup(String testName){
        MobileDriver mobileDriver = new MobileDriver();
        mobileDriver.setupMobileDriver(testName);
    }

    public void remoteDriverSetup(String platform, String URL) throws IOException {
        RemoteDriver remoteDriver = new RemoteDriver();
        remoteDriver.setupRemoteDriver(platform, URL);
    }

    public By fetchElement(String elementName){
        if(driver.get() instanceof RemoteWebDriver){
            return By.xpath(elementInstance.getElementValue(elementName, "web"));
        } else if (driver.get() instanceof AndroidDriver) {
            return By.xpath(elementInstance.getElementValue(elementName, "android"));
        } else if (driver.get() instanceof IOSDriver) {
            return By.xpath(elementInstance.getElementValue(elementName, "ios"));
        }else {
            System.err.println("Driver is not an instance of AndroidDriver or IOSDriver or RemoteWebDriver");
            return null;
        }
    }

    public List<WebElement> findElements(String elementName, Integer timeout){
        waitElementExist(elementName, timeout);
        return driver.get().findElements(fetchElement(elementName));
    }

    public Result verifyElementVisible(String elementName, Integer timeout){
        try{
            Assert.assertTrue(waitElementVisible(elementName, timeout), "Element "+elementName+" with locator: "+fetchElement(elementName));
            highlightElement(elementName, timeout);
            unHighlightElement(elementName, timeout);
            return new Result(true, "");
        }catch (Exception e){
            return new Result(false, e.getLocalizedMessage());
        }
    }

    public boolean verifyElementExist(String elementName, Integer timeout){
        try{
            Assert.assertTrue(waitElementExist(elementName, timeout), "Element "+elementName+" with locator: "+fetchElement(elementName));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void click(String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        driver.get().findElement(fetchElement(elementName)).click();
    }

    public void setText(String value, String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        driver.get().findElement(fetchElement(elementName)).sendKeys(value);
    }

    public void close(){
        driver.get().close();
    }

    public void quit(){
        driver.get().quit();
    }

    public String getText(String elementName, Integer timeout){
        waitElementExist(elementName, timeout);
        return driver.get().findElement(fetchElement(elementName)).getText();
    }

    public void scrollToView(String elementName, Integer timeout) {
        waitElementExist(elementName, timeout);
        JavascriptExecutor j = (JavascriptExecutor) driver.get();
        j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", driver.get().findElement(fetchElement(elementName)));
    }

    public void highlightElement(String elementName, Integer timeout){
        try{
            scrollToView(elementName, timeout);
            ((JavascriptExecutor) driver.get()).executeScript("arguments[0].style.border='3px solid lime'", driver.get().findElement(fetchElement(elementName)));
        }catch (Exception ignored){

        }
    }

     public void unHighlightElement(String elementName, Integer timeout){
        try{
            scrollToView(elementName, timeout);
            ((JavascriptExecutor) driver.get()).executeScript("arguments[0].style.removeProperty('border')", driver.get().findElement(fetchElement(elementName)));
        }catch (Exception ignored){

        }
    }

    public void screenshot() {
        if(driver.get() instanceof RemoteWebDriver){
            byte[] screenshot = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES);
            Hooks.getScenario().attach(screenshot, "image/png", "Screenshot");
        }else{
            TakesScreenshot screenshotDriver = (TakesScreenshot) driver.get();
            byte[] screenshot = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            getScenario().attach(screenshot, "image/png", "Screenshot");
        }

    }

    public void pressEnter() {
        Actions actions = new Actions(driver.get());
        actions.sendKeys(Keys.ENTER);
        actions.perform();
    }

    public void assertPageTitle(String title) {
        Assert.assertEquals(title, driver.get().getTitle());
    }

    public void navigateToURL(String URL) {
        driver.get().get(globalDeviceParameter.get(0).get(URL));
    }
}
