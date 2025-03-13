package builds.actions;

import builds.utilities.BrowserInstance;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.*;
import org.testng.asserts.SoftAssert;
import workDirectory.stepDefinitions.Hooks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BrowserActions extends BrowserInstance{

    SoftAssert softAssert = new SoftAssert();
    
    public void browserSetup(String browserType, String URL){
        browserInit(browserType, URL);
    }

    public String getPlatform(){
        if(isWebDriver.get()){
            return "web";
        }else{
            throw new RuntimeException("Check platform properly. Only allowed \"web\" if current session is web browser");
        }
    }

    public By fetchElement(String elementName){
        return By.xpath(getElementValue(elementName, getPlatform()));
    }

    public List<WebElement> fetchElements(String elementName){
        return appiumDriver.get().findElements(By.xpath(getElementValue(elementName, getPlatform())));
    }

    public boolean verifyElementVisible(String elementName){
        try{
            softAssert.assertTrue(waitElement(elementName), "Element "+elementName+" with locator: "+getElementValue(elementName, getPlatform()));
            highlightElement(elementName);
            unHighlightElement(elementName);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean verifyElementExist(String elementName){
        List<WebElement> elements = webDriver.get().findElements(fetchElement(elementName));
        if(!elements.isEmpty()){
            softAssert.assertTrue(true, "Element is Exist: "+ fetchElement(elementName));
            return true;
        }else{
            softAssert.fail("Element is not Exist: "+ fetchElement(elementName));
            return false;
        }
    }

    public void click(String elementName) {
        waitElement(elementName);
        webDriver.get().findElement(fetchElement(elementName)).click();
    }

    public void setText(String value, String elementName) {
        waitElement(elementName);
        webDriver.get().findElement(fetchElement(elementName)).sendKeys(value);
    }

    public void close(){
        webDriver.get().close();
    }

    public void quit(){
        webDriver.get().quit();
    }

    public String getText(String elementName){
        return webDriver.get().findElement(fetchElement(elementName)).getText();
    }

    public boolean waitElement(String elementName) {
        try{
            WebDriverWait wait = new WebDriverWait(webDriver.get(), Duration.ofSeconds(Long.parseLong(globalDeviceParameter.get(0).get("timeOut"))));
            wait.until(ExpectedConditions.presenceOfElementLocated(fetchElement(elementName)));
            scrollToView(elementName);
            wait.until((ExpectedConditions.visibilityOf(webDriver.get().findElement(fetchElement(elementName)))));
            return true;
        }catch (Exception e){
            softAssert.fail("Element is not visible or exist in DOM \n"+e.getMessage());
            return false;
        }
    }

    public void scrollToView(String elementName) {
        JavascriptExecutor j = (JavascriptExecutor) webDriver.get();
        j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", webDriver.get().findElement(fetchElement(elementName)));
    }

    public void highlightElement(String elementName){
        try{
            ((JavascriptExecutor) webDriver.get()).executeScript("arguments[0].style.border='3px solid lime'", webDriver.get().findElement(fetchElement(elementName)));
        }catch (Exception e){
            System.err.println(e.getCause());
        }
    }

     public void unHighlightElement(String elementName){
        try{
            ((JavascriptExecutor) webDriver.get()).executeScript("arguments[0].style.removeProperty('border')", webDriver.get().findElement(fetchElement(elementName)));
        }catch (Exception e){
            System.err.println(e.getCause());
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

    public void openURL(String url) {
        webDriver.get().get(url);
    }

    public void pressEnter() {
        Actions actions = new Actions(webDriver.get());
        actions.sendKeys(Keys.ENTER);
        actions.perform();
    }

    public void assertPageTitle(String title) {
        softAssert.assertTrue(webDriver.get().getTitle().equals(title));
    }

    public void navigateToURL(String URL) {
        webDriver.get().get(globalDeviceParameter.get(0).get(URL));
    }
}
