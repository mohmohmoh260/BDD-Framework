package builds.actions;

import builds.driver.BrowserInstance;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.*;
import org.testng.Assert;
import workDirectory.stepDefinitions.Hooks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BrowserActions extends BrowserInstance{

    public String getPlatform(){
        if(isWebDriver.get()){
            return "web";
        }else{
            throw new RuntimeException("Check platform properly. Only allowed \"web\" if current session is web browser");
        }
    }

    public boolean waitElementExist(String elementName, Integer timeout) {
        try{
            if (timeout == null) {
                timeout = Integer.parseInt(globalDeviceParameter.get(0).get("timeOut"));
            }
            WebDriverWait wait = new WebDriverWait(webDriver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
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
            WebDriverWait wait = new WebDriverWait(webDriver.get(), Duration.ofSeconds(Long.valueOf(timeout)));
            wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
            wait.until((ExpectedConditions.visibilityOf(webDriver.get().findElement(fetchElement(elementName)))));
            return true;
        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void browserSetup(String browserType, String URL){
        setupBrowserDriver(browserType, URL);
    }

    public By fetchElement(String elementName){
        return By.xpath(getElementValue(elementName, getPlatform()));
    }

    public List<WebElement> findElements(String elementName, Integer timeout){
        waitElementExist(elementName, timeout);
        return appiumDriver.get().findElements(By.xpath(getElementValue(elementName, getPlatform())));
    }

    public boolean verifyElementVisible(String elementName, Integer timeout){
        try{
            Assert.assertTrue(waitElementVisible(elementName, timeout), "Element "+elementName+" with locator: "+getElementValue(elementName, getPlatform()));
            highlightElement(elementName, timeout);
            unHighlightElement(elementName, timeout);
            return true;
        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public boolean verifyElementExist(String elementName, Integer timeout){
        try{
            Assert.assertTrue(waitElementExist(elementName, timeout), "Element "+elementName+" with locator: "+getElementValue(elementName, getPlatform()));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void click(String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        webDriver.get().findElement(fetchElement(elementName)).click();
    }

    public void setText(String value, String elementName, Integer timeout) {
        waitElementVisible(elementName, timeout);
        webDriver.get().findElement(fetchElement(elementName)).sendKeys(value);
    }

    public void close(){
        webDriver.get().close();
    }

    public void quit(){
        webDriver.get().quit();
    }

    public String getText(String elementName, Integer timeout){
        waitElementExist(elementName, timeout);
        return webDriver.get().findElement(fetchElement(elementName)).getText();
    }

    public void scrollToView(String elementName, Integer timeout) {
        waitElementExist(elementName, timeout);
        JavascriptExecutor j = (JavascriptExecutor) webDriver.get();
        j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", webDriver.get().findElement(fetchElement(elementName)));
    }

    public void highlightElement(String elementName, Integer timeout){
        try{
            scrollToView(elementName, timeout);
            ((JavascriptExecutor) webDriver.get()).executeScript("arguments[0].style.border='3px solid lime'", webDriver.get().findElement(fetchElement(elementName)));
        }catch (Exception ignored){

        }
    }

     public void unHighlightElement(String elementName, Integer timeout){
        try{
            scrollToView(elementName, timeout);
            ((JavascriptExecutor) webDriver.get()).executeScript("arguments[0].style.removeProperty('border')", webDriver.get().findElement(fetchElement(elementName)));
        }catch (Exception ignored){

        }
    }

     public void switchToTab(int tab){
        ArrayList<String> newTb = new ArrayList<>(webDriver.get().getWindowHandles());
        //switch to new tab
        webDriver.get().switchTo().window(newTb.get(tab));
    }

     public void switchToMainTab(){
        ArrayList<String> newTb = new ArrayList<>(webDriver.get().getWindowHandles());
        webDriver.get().switchTo().window(newTb.get(0));
    }

    public void selectDropdownByText(String elementName, String text){
        Select select= new Select(webDriver.get().findElement(fetchElement(elementName)));
        select.selectByVisibleText(text);
    }

    public void selectDropdownByIndex(String elementName, int index){
        Select select= new Select(webDriver.get().findElement(fetchElement(elementName)));
        select.selectByIndex(index);
    }

    public void selectDropdownByValue(String elementName, String text){
        Select select= new Select(webDriver.get().findElement(fetchElement(elementName)));
        select.selectByValue(text);
    }

    public void screenshot() {
        byte[] screenshot = ((TakesScreenshot) webDriver.get()).getScreenshotAs(OutputType.BYTES);
        Hooks.getScenario().attach(screenshot, "image/png", "Screenshot");
    }

    public void pressEnter() {
        Actions actions = new Actions(webDriver.get());
        actions.sendKeys(Keys.ENTER);
        actions.perform();
    }

    public void assertPageTitle(String title) {
        Assert.assertEquals(title, webDriver.get().getTitle());
    }

    public void navigateToURL(String URL) {
        webDriver.get().get(globalDeviceParameter.get(0).get(URL));
    }
}
