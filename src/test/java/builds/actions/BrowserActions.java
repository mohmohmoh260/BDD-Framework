package builds.actions;

import builds.main.CucumberRun;
import builds.utilities.BrowserInstance;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static workDirectory.stepDefinitions.Hooks.getScenario;

public class BrowserActions extends BrowserInstance{

    public void browserSetup(String browserType){
        browserInit(browserType);
    }
    private SoftAssert softAssert = new SoftAssert();
    public void assertElementDisplayed(WebElement element){
        softAssert.assertTrue(element.isDisplayed());
        highlightElement(element);
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
            System.err.println("Caught Exception: Check the xpath type in page object");
        }
        return by;
    }

    public void click(WebElement element) {
       getWebDriver().findElement(getBy(element)).click();
    }

    public void sendKeys(WebElement element, String input) {
        getWebDriver().findElement(getBy(element)).sendKeys(input);
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

    public void waitElement(WebElement element, ITestContext context) {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(Long.valueOf(context.getCurrentXmlTest().getParameter("browserType"))));
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
            System.err.println(e.getCause());
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
        Select select= new Select(getWebDriver().findElement(getBy(element)));
        select.selectByVisibleText(text);
    }

    public void selectDropdownByIndex(WebElement element, int index){
        Select select= new Select(getWebDriver().findElement(getBy(element)));
        select.selectByIndex(index);
    }

    public void selectDropdownByValue(WebElement element, String text){
        Select select= new Select(getWebDriver().findElement(getBy(element)));
        select.selectByValue(text);
    }

    public void screenshot() {
        byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
        getScenario().attach(screenshot, "image/png", "Screenshot");
    }

    public void openURL(String url) {
        getWebDriver().get(url);
    }

    public void pressEnter() {
        Actions actions = new Actions(getWebDriver());
        actions.sendKeys(Keys.ENTER);
        actions.perform();
    }

    public void assertPageTitle(String title) {
        softAssert.assertTrue(getWebDriver().getTitle().equals(title));
    }
}
