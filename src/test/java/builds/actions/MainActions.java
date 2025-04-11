package builds.actions;

import builds.driver.BrowserDriver;
import builds.driver.MainDriver;
import builds.driver.MobileDriver;
import builds.driver.RemoteDriver;
import builds.extent.ExtentManager;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public abstract class MainActions extends MainDriver {

    protected static final ThreadLocal<Boolean> toExecute = ThreadLocal.withInitial(() -> true);
    protected static final ThreadLocal<HashMap<String, String>> variables = ThreadLocal.withInitial(HashMap::new);
    protected static final ThreadLocal<Boolean> isSnippet = ThreadLocal.withInitial(() -> false);

    public boolean waitElementExist(String elementName, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
        return true;
    }

    public boolean waitElementVisible(String elementName, Integer timeout){
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        //wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
        wait.until((ExpectedConditions.visibilityOf(driver.get().findElement(fetchElement(elementName)))));
        return true;
    }

    public boolean waitElementNotVisible(String elementName, Integer timeout){
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until((ExpectedConditions.invisibilityOf(driver.get().findElement(fetchElement(elementName)))));
        return true;
    }

    public void browserDriverSetup(String browserType, String URL){
        BrowserDriver browserDriver = new BrowserDriver();
        browserDriver.setupBrowserDriver(browserType, URL);
    }

    public void mobileDriverSetup(String testName){
        MobileDriver mobileDriver = new MobileDriver();
        mobileDriver.setupMobileDriver(testName);
    }

    public void remoteDriverSetup(String parentKey, String URL) throws IOException {
        RemoteDriver remoteDriver = new RemoteDriver();
        remoteDriver.setupRemoteDriver(parentKey, URL);
    }

    public By fetchElement(String elementName) {
        String xpath;

        if (driver.get() instanceof AndroidDriver) {  // ✅ Check Android first
            xpath = getElementValue(elementName, "android");
        } else if (driver.get() instanceof IOSDriver) {
            xpath = getElementValue(elementName, "ios");
        } else if (driver.get() instanceof RemoteWebDriver) {
            xpath = getElementValue(elementName, "web");
        } else {
            throw new IllegalStateException("❌ Unsupported driver type: " + driver.get().getClass().getSimpleName());
        }
        return By.xpath(xpath);
    }

    public List<WebElement> findElements(String elementName, Integer timeout){
        waitElementExist(elementName, timeout);
        return driver.get().findElements(fetchElement(elementName));
    }

    public Boolean verifyElementVisible(String elementName, Integer timeout){
        try{
            Assert.assertTrue(waitElementVisible(elementName, timeout), "Element "+elementName+" with locator: "+fetchElement(elementName));
            if(driver.get() instanceof RemoteWebDriver){
                BrowserActions browserActions = new BrowserActions();
                browserActions.highlightElement(elementName, timeout);
                browserActions.unHighlightElement(elementName, timeout);
            }
        }catch (Throwable t){
            System.err.println(t.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public Boolean verifyElementNotVisible(String elementName, Integer timeout){
        try{
            Assert.assertTrue(waitElementNotVisible(elementName, timeout), "Element "+elementName+" with locator: "+fetchElement(elementName));
            if(driver.get() instanceof RemoteWebDriver){
                BrowserActions browserActions = new BrowserActions();
                browserActions.highlightElement(elementName, timeout);
                browserActions.unHighlightElement(elementName, timeout);
            }
        }catch (Throwable t){
            System.err.println(t.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public Boolean verifyElementExist(String elementName, Integer timeout){
        try{
            Assert.assertTrue(waitElementExist(elementName, timeout), "Element "+elementName+" with locator: "+fetchElement(elementName));
            if(driver.get() instanceof RemoteWebDriver){
                BrowserActions browserActions = new BrowserActions();
                browserActions.highlightElement(elementName, timeout);
                browserActions.unHighlightElement(elementName, timeout);
            }
        }catch (Throwable t){
            System.err.println(t.getLocalizedMessage());
            return false;
        }
        return true;

    }

    public void verifyEquals(Object actual, Object expected){
        Assert.assertEquals(actual, expected);
    }

    public void click(String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        driver.get().findElement(fetchElement(elementName)).click();
    }

    public void setText(String value, String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        driver.get().findElement(fetchElement(elementName)).sendKeys(value);
    }

    public String getText(String elementName, Integer timeout){
        String actualText = "";
        waitElementExist(elementName, timeout);
        actualText =  driver.get().findElement(fetchElement(elementName)).getText();
        return actualText;
    }

    public static Media takeScreenshot() {
        if (driver.get() == null) {
            return null; // Return null to prevent errors
        }

        try {
            String relativePath = ExtentManager.baseScreenshotFolder + System.currentTimeMillis() + ".png";
            String absolutePath = new File(relativePath).getAbsolutePath();  // Ensure correct path

            File srcFile = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(absolutePath));

            return MediaEntityBuilder.createScreenCaptureFromPath(absolutePath).build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void navigateToURL(String URL) {
        driver.get().get(globalDeviceParameter.get().get(0).get(URL));
    }

    public void close(){
        driver.get().close();
    }

    public void quit(){
        driver.get().quit();
    }

    protected void assertEquals(String actualText, String expectedVariableValue, String failMessage){
       if(actualText.equals(expectedVariableValue)){
            if(isSnippet.get()){
                ExtentManager.getNodeExtent().pass(takeScreenshot());
            }else{
                ExtentManager.getExtent().pass(takeScreenshot());
            }
       }else {
           if(isSnippet.get()){
               ExtentManager.getNodeExtent().fail(failMessage, takeScreenshot());
           }else{
               ExtentManager.getExtent().pass(failMessage, takeScreenshot());
           }
       }

    }
}
