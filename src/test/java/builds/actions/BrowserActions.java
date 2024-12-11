package builds.actions;

import builds.utilities.BaseTest;
import builds.utilities.GlobalProperties;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import workDirectory.stepDefinitions.Hooks;
import org.openqa.selenium.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BrowserActions extends BaseTest{
    private static SoftAssert softAssert = new SoftAssert();
    public  static void assertElementDisplayed(WebElement element){
        softAssert.assertTrue(element.isDisplayed());
        highlightElement(element);
        screenshot();
        unHighlightElement(element);
    }

    public  static void assertElementExist(WebElement element){
        List<WebElement> elements = webDriver.findElements(getBy(element));
        if(!elements.isEmpty()){
            softAssert.assertTrue(true);
        }else{
            softAssert.fail("Element is not Exist: "+getBy(element));
        }
    }

    public  static By getBy(WebElement element){
        String xpath = element.toString();
        xpath = (xpath.substring(0, xpath.length() - 1).split("-> ")[1]).replace("xpath: ","");
        By by = By.xpath(xpath);
        return by;
    }

    public  static void click(WebElement element) {
       element.click();
    }

    public  static void sendKeys(WebElement element, String input) {
        element.sendKeys(input);
    }

    public void close(){
        webDriver.close();
    }

    public void quit(){
        webDriver.quit();
    }

    public  String getText(WebElement element){
        return element.getText();
    }

//    public  static clear(WebElement element){
//        element.clear();
//    }

    public  static void waitElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(Long.valueOf(GlobalProperties.getGlobalVariablesProperties().getProperty("max.timeout.in.seconds"))));
        wait.until(ExpectedConditions.presenceOfElementLocated(getBy(element)));
        scrollToView(element);
    }

     static void scrollToView(WebElement element) {
        JavascriptExecutor j = (JavascriptExecutor)webDriver;
        j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
    }

     static void highlightElement(WebElement element){
        try{
            ((JavascriptExecutor)webDriver).executeScript("arguments[0].style.border='3px solid lime'", element);
        }catch (Exception e){
            System.out.println(e.getCause());
        }
    }

    static void unHighlightElement(WebElement element){
        try{
            ((JavascriptExecutor)webDriver).executeScript("arguments[0].style.removeProperty('border')", element);
        }catch (Exception e){
            System.out.println(e.getCause());
        }
    }

     void switchToTab(int tab){
        ArrayList<String> newTb = new ArrayList<>(webDriver.getWindowHandles());
        //switch to new tab
        webDriver.switchTo().window(newTb.get(tab));
    }

     void switchToMainTab(){
        ArrayList<String> newTb = new ArrayList<>(webDriver.getWindowHandles());
        //switch to new tab
        webDriver.switchTo().window(newTb.get(0));
    }

//    public  static selectDropdownByText(WebElement element, String text){
//        Select select= new Select(element);
//        select.selectByVisibleText(text);
//
//    }
//
//    public  static selectDropdownByIndex(WebElement element, int index){
//        Select select= new Select(element);
//        select.selectByIndex(index);
//    }
//
//    public  static selectDropdownByValue(WebElement element, String text){
//        Select select= new Select(element);
//        select.selectByValue(text);
//    }

    public  static void screenshot() {
        final byte [] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        Hooks.getScenario().attach(screenshot, "image/png", "");
    }

    public  static void openURL(String globalVariableName) {
        webDriver.get(GlobalProperties.getGlobalVariablesProperties().getProperty(globalVariableName));
    }

    public  static void pressEnter() {
        Actions actions = new Actions(webDriver);
        actions.sendKeys(Keys.ENTER);
        actions.perform();
    }

    public  static void assertPageTitle(String expectedTitle) {
        String actualTitle = webDriver.getTitle();
        Assert.assertEquals(actualTitle,expectedTitle);
        screenshot();
    }
}
