package builds.actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.ArrayList;

public class BrowserActions extends MainActions{

    public void switchToTab(int tab){
        ArrayList<String> newTb = new ArrayList<>(driver.get().getWindowHandles());
        //switch to new tab
        driver.get().switchTo().window(newTb.get(tab));
    }

    public void switchToMainTab(){
        ArrayList<String> newTb = new ArrayList<>(driver.get().getWindowHandles());
        driver.get().switchTo().window(newTb.get(0));
    }

    public void selectDropdownByText(String elementName, String text){
        Select select= new Select(driver.get().findElement(fetchElement(elementName)));
        select.selectByVisibleText(text);
    }

    public void selectDropdownByIndex(String elementName, int index){
        Select select= new Select(driver.get().findElement(fetchElement(elementName)));
        select.selectByIndex(index);
    }

    public void selectDropdownByValue(String elementName, String text){
        Select select= new Select(driver.get().findElement(fetchElement(elementName)));
        select.selectByValue(text);
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

    public void pressEnter() {
        Actions actions = new Actions(driver.get());
        actions.sendKeys(Keys.ENTER);
        actions.perform();
    }

    public void assertPageTitle(String title) {
        Assert.assertEquals(title, driver.get().getTitle());
    }
}
