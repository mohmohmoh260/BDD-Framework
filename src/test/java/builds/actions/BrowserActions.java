package builds.actions;

import builds.utilities.BrowserInstance;
import builds.utilities.GlobalProperties;
import io.cucumber.java.Scenario;
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

public class BrowserActions extends BrowserInstance{

    public void browserSetup(){
        browserInit();
    }
    private SoftAssert softAssert = new SoftAssert();
    public void assertElementDisplayed(WebElement element){
        softAssert.assertTrue(element.isDisplayed());
        highlightElement(element);
        Hooks hooks = new Hooks();
        hooks.getScenario();
        unHighlightElement(element);
    }

    public void assertElementExist(WebElement element){
        List<WebElement> elements = getWebDriver().findElements(getBy(element));
        if(!elements.isEmpty()){
            softAssert.assertTrue(true);
        }else{
            softAssert.fail("Element is not Exist: "+getBy(element));
        }
    }

    public By getBy(WebElement element){
        String xpath = element.toString();
        xpath = (xpath.substring(0, xpath.length() - 1).split("-> ")[1]).replace("xpath: ","");
        By by = By.xpath(xpath);
        return by;
    }

    public void click(WebElement element) {
       element.click();
    }

    public synchronized void sendKeys(WebElement element, String input) {
        element.sendKeys(input);
    }

    public void close(){
        getWebDriver().close();
    }

    public void quit(){
        getWebDriver().quit();
    }

    public String getText(WebElement element){
        return element.getText();
    }

    public void waitElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(Long.valueOf(GlobalProperties.getGlobalVariablesProperties().getProperty("max.timeout.in.seconds"))));
        wait.until(ExpectedConditions.presenceOfElementLocated(getBy(element)));
        scrollToView(element);
    }

    public void scrollToView(WebElement element) {
        JavascriptExecutor j = (JavascriptExecutor) getWebDriver();
        j.executeScript ("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
    }

    public void highlightElement(WebElement element){
        try{
            ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].style.border='3px solid lime'", element);
        }catch (Exception e){
            System.out.println(e.getCause());
        }
    }

     public void unHighlightElement(WebElement element){
        try{
            ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].style.removeProperty('border')", element);
        }catch (Exception e){
            System.out.println(e.getCause());
        }
    }

     public void switchToTab(int tab){
        ArrayList<String> newTb = new ArrayList<>(getWebDriver().getWindowHandles());
        //switch to new tab
        getWebDriver().switchTo().window(newTb.get(tab));
    }

     public void switchToMainTab(){
        ArrayList<String> newTb = new ArrayList<>(getWebDriver().getWindowHandles());
        getWebDriver().switchTo().window(newTb.get(0));
    }

    public void selectDropdownByText(WebElement element, String text){
        Select select= new Select(element);
        select.selectByVisibleText(text);

    }

    public void selectDropdownByIndex(WebElement element, int index){
        Select select= new Select(element);
        select.selectByIndex(index);
    }

    public void selectDropdownByValue(WebElement element, String text){
        Select select= new Select(element);
        select.selectByValue(text);
    }

    public synchronized void screenshot() {
        Scenario scenario = Hooks.getScenario();
            byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot");
            System.err.println("Scenario is null; cannot attach screenshot.");
    }

    public void openURL(String globalVariableName) {
        getWebDriver().get(GlobalProperties.getGlobalVariablesProperties().getProperty(globalVariableName));
    }

    public void pressEnter() {
        Actions actions = new Actions(getWebDriver());
        actions.sendKeys(Keys.ENTER);
        actions.perform();
    }

    public void assertPageTitle(String expectedTitle) {
        String actualTitle = getWebDriver().getTitle();
        Assert.assertEquals(actualTitle,expectedTitle);
        screenshot();
    }
}
