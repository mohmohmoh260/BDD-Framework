package workDirectory.stepDefinitions;

import builds.actions.BrowserActions;
import builds.actions.MobileActions;
import builds.utilities.DriverType;
import io.cucumber.java.Scenario;
import io.cucumber.java.sl.In;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.*;

public class CommonMethods extends DriverType {

    protected static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> toExecute = ThreadLocal.withInitial(() -> true);
    private static final ThreadLocal<Map<String, String>> variables = ThreadLocal.withInitial(HashMap::new);
    protected final MobileActions mobileActions = new MobileActions();
    protected final BrowserActions browserActions = new BrowserActions();

    protected void toExecuteChecker(String methodName, List<Object> param, Integer timeout){
        System.out.println(methodName);
        switch (methodName){
            case "ifNumberIsBiggerThanNumber":
                if((Integer) param.get(0)>(Integer) param.get(1)){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
                break;
            case "ifNumberIsSmallerThanNumber":
                if((Integer) param.get(0)<(Integer) param.get(1)){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
                break;
            case "ifElementIsNotVisible":
                if(!mobileActions.verifyElementVisible((String) param.get(0), timeout)){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
            case "ifElementIsVisible":
                if(mobileActions.verifyElementVisible((String) param.get(0), timeout)){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
            default:
                System.out.println("if statement is not exist, please check if the method name is exist in toExecuteChecker method");
        }
    }

    protected void endIf(){
       toExecute.set(true);
    }

    protected void mobileSetup(String testName){
        mobileActions.mobileSetup(testName);
    }

    protected void browserSetup(String browserType, String URL){
        browserActions.browserSetup(browserType, URL);
    }

    protected void setText(String value, String elementName){
        if(isAppiumDriver.get()){
            mobileActions.setText(value, elementName, null);
        }else {
            browserActions.setText(value, elementName, null);
        }
    }

    protected void navigateToURL(String URL){
        if(isAppiumDriver.get()){
            mobileActions.navigateToURL(URL);
        }else {
            browserActions.navigateToURL(URL);
        }
    }

    protected void takeScreenshot(){
        if(isAppiumDriver.get()){
            mobileActions.screenshot();
        }else {
            browserActions.screenshot();
        }
    }

    protected void click(String elementName){
        if(isAppiumDriver.get()){
            mobileActions.click(elementName, null);
        }else {
            browserActions.click(elementName, null);
        }
    }

    protected void verifyElementVisible(String elementName, Integer timeout){
        if(isAppiumDriver.get()){
            mobileActions.verifyElementVisible(elementName, timeout);
        }else {
            browserActions.verifyElementVisible(elementName, timeout);
        }
    }

    protected void waitElementVisible(String elementName, Integer timeout){
        if(isAppiumDriver.get()){
            mobileActions.waitElementVisible(elementName, timeout);
        }else {
            browserActions.waitElementVisible(elementName, timeout);
        }
    }

    protected void getTextFromAndSetIntoVariable(String elementName, String variableName, Integer timeout){
        if(isAppiumDriver.get()){
            variables.get().put(variableName, mobileActions.getText(elementName, timeout));
            currentScenario.get().log("Text :"+mobileActions.getText(elementName, timeout)+" is set into variable "+variableName);
        }else {
            variables.get().put(variableName, browserActions.getText(elementName, timeout));
            currentScenario.get().log("Text :"+browserActions.getText(elementName, timeout)+" is set into variable "+variableName);
        }
    }

    protected void verifyTextIsEqualsToVariable(String expectedText, String variableName){
        Assert.assertEquals(expectedText, variables.get().get(variableName), "Expected text "+expectedText+" is not equals to actual text "+variables.get().get(variableName));
    }

}
