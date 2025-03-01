package builds.actions;

import builds.utilities.BrowserInstance;
import builds.utilities.TestNGXmlParser;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static workDirectory.stepDefinitions.Hooks.getScenario;

public class BrowserActions extends BrowserInstance{
    private TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
    private SoftAssert softAssert = new SoftAssert();

    public void browserSetup(String browserType){
        browserInit(browserType);
    }

    public void assertElementDisplayed(WebElement element){
        softAssert.assertTrue(element.isDisplayed());
        highlightElement(element);
        unHighlightElement(element);
    }

    public void assertElementExist(WebElement element){
        List<WebElement> elements = webDriver.get().findElements(getBy(element));
        if(!elements.isEmpty()){
            softAssert.assertTrue(true);
        }else{
            softAssert.fail("Element is not Exist: "+getBy(element));
        }
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
            System.err.println("Caught Exception: Check the WebElement to be split properly: "+ element);
        }
        return by;
    }

    public void click(WebElement element) {
       webDriver.get().findElement(getBy(element)).click();
    }

    public void sendKeys(WebElement element, String input) {
        webDriver.get().findElement(getBy(element)).sendKeys(input);
    }

    public void close(){
        webDriver.get().close();
    }

    public void quit(){
        webDriver.get().quit();
    }

    public String getText(WebElement element){
        return element.getText();
    }

    public void waitElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(webDriver.get(), Duration.ofSeconds(Long.parseLong(testNGXmlParser.getGlobalParameters().get(0).get("timeOut"))));
        wait.until(ExpectedConditions.presenceOfElementLocated(getBy(element)));
        scrollToView(element);
        wait.until((ExpectedConditions.visibilityOf(element)));
    }

    public void scrollToView(WebElement element) {
        JavascriptExecutor j = (JavascriptExecutor) webDriver.get();
        j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
    }

    public void highlightElement(WebElement element){
        try{
            ((JavascriptExecutor) webDriver.get()).executeScript("arguments[0].style.border='3px solid lime'", element);
        }catch (Exception e){
            System.err.println(e.getCause());
        }
    }

     public void unHighlightElement(WebElement element){
        try{
            ((JavascriptExecutor) webDriver.get()).executeScript("arguments[0].style.removeProperty('border')", element);
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

    public void selectDropdownByText(WebElement element, String text){
        Select select= new Select(webDriver.get().findElement(getBy(element)));
        select.selectByVisibleText(text);
    }

    public void selectDropdownByIndex(WebElement element, int index){
        Select select= new Select(webDriver.get().findElement(getBy(element)));
        select.selectByIndex(index);
    }

    public void selectDropdownByValue(WebElement element, String text){
        Select select= new Select(webDriver.get().findElement(getBy(element)));
        select.selectByValue(text);
    }

    public void screenshot() {
        byte[] screenshot = ((TakesScreenshot) webDriver.get()).getScreenshotAs(OutputType.BYTES);
        getScenario().attach(screenshot, "image/png", "Screenshot");
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
}
