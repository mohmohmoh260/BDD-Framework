package builds.actions;

import builds.driver.BrowserDriver;
import builds.driver.MainDriver;
import builds.driver.MobileDriver;
import builds.driver.RemoteDriver;
import builds.extent.ExtentManager;
import builds.listener.StepListener;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public abstract class MainActions extends MainDriver {

    protected static final ThreadLocal<LinkedList<Boolean>> toExecute = ThreadLocal.withInitial(() -> new LinkedList<>(List.of(true)));
    protected static final ThreadLocal<HashMap<String, String>> variables = ThreadLocal.withInitial(HashMap::new);
    protected static final ThreadLocal<String> currentSnippetStep = ThreadLocal.withInitial(() -> "");
    protected static final ThreadLocal<Boolean> isSnippet = ThreadLocal.withInitial(() -> false);
    protected static final ThreadLocal<Boolean> stepStatus = ThreadLocal.withInitial(() -> true);

    protected Throwable waitElementExist(String elementName, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
        return null;
    }

    protected Throwable waitElementExist(By by, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return null;
    }

    protected Throwable waitElementNotExist(String elementName, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
        return null;
    }

    protected Throwable waitElementNotExist(By by, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return null;
    }

    protected Throwable waitElementVisible(String elementName, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until((ExpectedConditions.visibilityOf(driver.get().findElement(fetchElement(elementName)))));
        return null;
    }

    protected Throwable waitElementVisible(By by, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until((ExpectedConditions.visibilityOf(driver.get().findElement(by))));
        return null;
    }

    protected Throwable waitElementNotVisible(String elementName, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until((ExpectedConditions.invisibilityOf(driver.get().findElement(fetchElement(elementName)))));
        return null;
    }

    protected Throwable waitElementNotVisible(By by, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until((ExpectedConditions.invisibilityOf(driver.get().findElement(by))));
        return null;
    }

    protected Throwable waitElementClickable(String elementName, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until((ExpectedConditions.elementToBeClickable(fetchElement(elementName))));
        return null;
    }

    protected Throwable waitElementClickable(By by, Integer timeout) {
        if (timeout == null) {
            timeout = Integer.parseInt(globalDeviceParameter.get().get(0).get("timeOut"));
        }
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
        wait.until((ExpectedConditions.elementToBeClickable(by)));
        return null;
    }

    protected By fetchElement(String elementName) {
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

    protected List<WebElement> findElements(String elementName) {
        return driver.get().findElements(fetchElement(elementName));
    }

    protected List<WebElement> findElements(By by) {
        return driver.get().findElements(by);
    }

    public static Media takeScreenshot() {
        if (driver.get() == null) {
            return null; // Return null to prevent errors
        }
        if (globalDeviceParameter.get().get(0).get("screenshotEveryStep").equals("true") ||
        currentSnippetStep.get().contains("And take screenshot") ||
        currentSnippetStep.get().contains("if text") ||
        currentSnippetStep.get().contains("if element ") ||
        currentSnippetStep.get().contains("verify text ") ||
        currentSnippetStep.get().contains("verify element ") ||
        StepListener.gherkinStep.get().contains("And take screenshot") ||
        StepListener.gherkinStep.get().contains("if text ") ||
        StepListener.gherkinStep.get().contains("if element ") ||
        StepListener.gherkinStep.get().contains("verify text ") ||
        StepListener.gherkinStep.get().contains("verify element ")) {

            String relativePath;
            String absolutePath = "";
            try {
                relativePath = ExtentManager.baseScreenshotFolder + System.currentTimeMillis() + ".png";
                absolutePath = new File(relativePath).getAbsolutePath();

                File srcFile = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(srcFile, new File(absolutePath));

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            if (absolutePath.isEmpty()) {
                return null;
            } else {
                return MediaEntityBuilder.createScreenCaptureFromPath(absolutePath).build();
            }
        }else {
            return null;
        }
    }

    public static Media takeScreenshotFailed() {
        if (driver.get() == null) {
            return null; // Return null to prevent errors
        }
        String relativePath;
        String absolutePath = "";
        try {
            relativePath = ExtentManager.baseScreenshotFolder + System.currentTimeMillis() + ".png";
            absolutePath = new File(relativePath).getAbsolutePath();

            File srcFile = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(absolutePath));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (absolutePath.isEmpty()) {
            return null;
        } else {
            return MediaEntityBuilder.createScreenCaptureFromPath(absolutePath).build();
        }
    }

    protected void writeReportPassed(){
        if(isSnippet.get()){
            ExtentManager.bufferLog(Status.PASS, currentSnippetStep.get(), takeScreenshot());
        }
    }

    protected void writeReportFailed(Throwable t){
        if(isSnippet.get()){
            ExtentManager.bufferLog(Status.FAIL, "<span style='color:red'>" + currentSnippetStep.get() + "</span><br><br>" + t.getMessage(), takeScreenshotFailed());
        }else{
            stepStatus.set(false);
        }
    }

    protected Throwable verifyElementVisible(String elementName, Integer timeout){
        try{
            waitElementVisible(elementName, timeout);
            writeReportPassed();
            return null;
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
            throw t;
        }
    }

    protected Throwable verifyElementNotVisible(String elementName, Integer timeout){
        try{
            waitElementNotVisible(elementName, timeout);
            writeReportPassed();
            return null;
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
            throw t;
        }
    }

    protected Throwable verifyElementNotVisible(By by, Integer timeout){
        try{
            waitElementNotVisible(by, timeout);
            writeReportPassed();
            return null;
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
            throw t;
        }
    }

    protected Boolean verifyElementExist(String elementName, Integer timeout){
        try {
            waitElementExist(elementName, timeout);
            writeReportPassed();
            return true;
        }catch (Throwable t) {
            writeReportFailed(t);
            throw t;
        }
    }

    protected Boolean verifyElementNotExist(String elementName, Integer timeout){
        try{
            waitElementNotExist(elementName, timeout);
            writeReportFailed(new Throwable("Element should not Exist"));
            return false;
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportPassed();
            throw t;
        }
    }

    protected Throwable verifyTextVisible(String text, Integer timeout){
        try{
            By by = By.xpath("//*[text()=\"" + text + "\"]");
            waitElementVisible(by, timeout);
            writeReportPassed();
            return null;
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
            throw t;
        }
    }

    protected Throwable verifyTextNotVisible(String text, Integer timeout){
        try{
            By by = By.xpath("//*[text()=\"" + text + "\"]");
            waitElementNotVisible(by, timeout);
            writeReportPassed();
            return null;
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
            throw t;
        }
    }

    protected void browserDriverSetup(String browserType, String URL){
        try {
            BrowserDriver browserDriver = new BrowserDriver();
            browserDriver.setupBrowserDriver(browserType, URL);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void mobileDriverSetup(String testName){
        try{
            MobileDriver mobileDriver = new MobileDriver();
            mobileDriver.setupMobileDriver(testName);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void remoteDriverSetup(String parentKey, String URL) throws IOException {
        try{
            RemoteDriver remoteDriver = new RemoteDriver();
            remoteDriver.setupRemoteDriver(parentKey, URL);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void clickElement(String elementName, Integer timeout) {
        try{
            waitElementVisible(elementName, timeout);
            waitElementClickable(elementName, timeout);
            WebElement element = driver.get().findElement(fetchElement(elementName));
            JavascriptExecutor js = (JavascriptExecutor) driver.get();
            js.executeScript("arguments[0].click();", element);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void clickText(String text, Integer timeout) {
        try{
            By by = By.xpath("//*[text()=\"" + text + "\"]");
            waitElementVisible(by, timeout);
            waitElementClickable(by, timeout);
            WebElement element = driver.get().findElement(by);
            JavascriptExecutor js = (JavascriptExecutor) driver.get();
            js.executeScript("arguments[0].click();", element);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void setText(String value, String elementName, Integer timeout) {
        try{
            waitElementVisible(elementName, timeout);
            driver.get().findElement(fetchElement(elementName)).sendKeys(value);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected String getText(String elementName, Integer timeout){
        String actualText = "";
        try{
            waitElementExist(elementName, timeout);
            actualText =  driver.get().findElement(fetchElement(elementName)).getText();
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
        return actualText;
    }

    protected void navigateToURL(String URL) {
        try{
            driver.get().get(globalDeviceParameter.get().get(0).get(URL));
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void close(){
        try{
            driver.get().close();
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void quit(){
        try{
            driver.get().quit();
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void assertEquals(String actualText, String expectedVariableValue){
        try{
            Assert.assertEquals(actualText, expectedVariableValue);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void switchToTab(int tab){
        try{
            ArrayList<String> newTb = new ArrayList<>(driver.get().getWindowHandles());
            driver.get().switchTo().window(newTb.get(tab));
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void switchToMainTab(){
        try{
            ArrayList<String> newTb = new ArrayList<>(driver.get().getWindowHandles());
            driver.get().switchTo().window(newTb.get(0));
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void selectDropdownByText(String elementName, String text){
        try{
            Select select= new Select(driver.get().findElement(fetchElement(elementName)));
            select.selectByVisibleText(text);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void selectDropdownByIndex(String elementName, int index){
        try{
            Select select= new Select(driver.get().findElement(fetchElement(elementName)));
            select.selectByIndex(index);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void selectDropdownByValue(String elementName, String value){
        try{
            Select select= new Select(driver.get().findElement(fetchElement(elementName)));
            select.selectByValue(value);
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

// ======================================================= Exception from writing into Report ======================================================

    protected void scrollToView(String elementName) {
        if(driver.get() instanceof RemoteWebDriver){
            JavascriptExecutor j = (JavascriptExecutor) driver.get();
            j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", driver.get().findElement(fetchElement(elementName)));
        }
    }

    public enum SwipeDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    protected void swipe(SwipeDirection direction, int startPercentage, int endPercentage) {
        int screenWidth = driver.get().manage().window().getSize().width;
        int screenHeight = driver.get().manage().window().getSize().height;

        int startX, startY, endX, endY;

        switch (direction) {
            case UP:
                startX = screenWidth / 2;
                startY = (int) (screenHeight * (1 - (startPercentage / 100.0)));
                endX = startX;
                endY = (int) (screenHeight * (1 - (endPercentage / 100.0)));
                break;

            case DOWN:
                startX = screenWidth / 2;
                startY = (int) (screenHeight * (startPercentage / 100.0));
                endX = startX;
                endY = (int) (screenHeight * (1 - (endPercentage / 100.0)));
                break;

            case LEFT:
                startX = (int) (screenWidth * (1 - (startPercentage / 100.0)));
                startY = screenHeight / 2;
                endX = (int) (screenHeight * (1 - (endPercentage / 100.0)));
                endY = startY;
                break;

            case RIGHT:
                startX = (int) (screenWidth * (startPercentage / 100.0));
                startY = screenHeight / 2;
                endX = (int) (screenHeight * (1 - (endPercentage / 100.0)));
                endY = startY;
                break;

            default:
                throw new IllegalArgumentException("Invalid swipe direction! Use: UP, DOWN, LEFT, or RIGHT.");
        }

        performSwipe(startX, startY, endX, endY);
    }

    protected void performSwipe(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((AppiumDriver) driver.get()).perform((Collections.singletonList(swipe)));
    }

// ===========================================================================================================================================

    protected void pressEnter() {
        try{
            if(driver.get() instanceof IOSDriver){
                driver.get().findElement(By.xpath("//XCUIElementTypeButton[@name=\"Go\"]")).click();
            }else if (driver.get() instanceof AndroidDriver){
                ((AndroidDriver) driver.get()).pressKey(new KeyEvent(AndroidKey.ENTER));
            }else if (driver.get() instanceof RemoteWebDriver){
                Actions actions = new Actions(driver.get());
                actions.sendKeys(Keys.ENTER);
                actions.perform();
            }else {
                throw new RuntimeException("Driver is not an instance of AndroidDriver or IOSDriver.");
            }
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void assertPageTitle(String title) {
        try{
            Assert.assertEquals(title, driver.get().getTitle());
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void hideKeyboard(){
        try{
            if (driver.get() instanceof AndroidDriver) {
                ((AndroidDriver) driver.get()).hideKeyboard();
            } else if (driver.get() instanceof IOSDriver) {
                ((IOSDriver) driver.get()).hideKeyboard();
            } else {
                throw new RuntimeException("Driver is not an instance of AndroidDriver or IOSDriver.");
            }
            writeReportPassed();
        }catch (Throwable t) {
            StepListener.lastStepError.set(t);
            writeReportFailed(t);
        }
    }

    protected void swipeDownToElement(String elementName, int startPercentage, int endPercentage, Integer timeout){
        boolean found = false;
        while(!found){
            try{
                verifyElementVisible(elementName, timeout);
                found = true;
            }catch (Throwable t){
                swipe(SwipeDirection.DOWN, startPercentage, endPercentage);
            }
        }
    }

    protected void swipeUpToElement(String elementName, int startPercentage, int endPercentage, Integer timeout){
        boolean found = false;
        while(!found){
           try{
               verifyElementVisible(elementName, timeout);
               found = true;
           }catch (Throwable t){
                swipe(SwipeDirection.UP, startPercentage, endPercentage);
           }
        }
    }

    protected void swipeRightToElement(String elementName, int startPercentage, int endPercentage, Integer timeout){
        boolean found = false;
        while(!found){
            try{
                verifyElementVisible(elementName, timeout);
                found = true;
            }catch (Throwable t){
                swipe(SwipeDirection.RIGHT, startPercentage, endPercentage);
            }
        }
    }

    protected void swipeLeftToElement(String elementName, int startPercentage, int endPercentage, Integer timeout){
        boolean found = false;
        while(!found){
            try{
                verifyElementVisible(elementName, timeout);
                found = true;
            }catch (Throwable t){
                swipe(SwipeDirection.LEFT, startPercentage, endPercentage);
            }
        }
    }
}
