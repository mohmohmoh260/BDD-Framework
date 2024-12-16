package builds.actions;

import builds.utilities.GlobalProperties;
import builds.utilities.MobileInstance;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class MobileActions extends MobileInstance{
    private  SoftAssert softAssert = new SoftAssert();

    public  void androidSetup(){
        androidInit();
    }

    public  void iOSSetup(){
        iOSInit();
    }

    public void assertElementDisplayed(WebElement element) {
        waitElement(element);
        Assert.assertTrue(element.isDisplayed());
        screenshot();
    }
    
    public void assertPageTitle(String title) {
        assertPageTitle(title);
    }

    public  void click(WebElement element) {
        waitElement(element);
        element.click();
    }

    public  void sendKeys(WebElement element, String input) {
        element.sendKeys(input);
    }

    public  void close() {
       getMobileDriver().close();
    }

    public  void quit() {
        getMobileDriver().quit();
    }

    public  List<WebElement> findElements(WebElement element){
        return findElements(element);
    }

    public String getText(WebElement element){
        return getText(element);
    }

//    public  clear(WebElement element){
//
//    }

//    public  hideKeyboard(){
//        MobileInstance.getMobileDriver();
//    }

     void scrollDownToElement(WebElement element){
        // ToDo
    }

     void scrollUPToElement(WebElement element){
        // ToDo
    }

    public   void waitElement(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getMobileDriver(), Duration.ofSeconds(Long.valueOf(GlobalProperties.getGlobalVariablesProperties().getProperty("max.timeout.in.seconds"))));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public   void screenshot() {
        final byte [] screenshot = ((TakesScreenshot) getMobileDriver()).getScreenshotAs(OutputType.BYTES);
//        Hooks hooks = new Hooks();
//        hooks.getScenario().attach(screenshot, "image/png", "");
    }

    public  void pressEnter() {
        ((AndroidDriver) getMobileDriver()).pressKey(new KeyEvent(AndroidKey.ENTER));
    }

}
