package builds.actions;

import org.openqa.selenium.support.ui.Select;

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
}
