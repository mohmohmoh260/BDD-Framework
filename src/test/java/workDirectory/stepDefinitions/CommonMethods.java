package workDirectory.stepDefinitions;

import builds.actions.BrowserActions;
import builds.actions.MobileActions;
import builds.main.CucumberRun;
import builds.utilities.ElementInstance;

import java.util.*;

public class CommonMethods extends ElementInstance {

    protected final MobileActions mobileActions = new MobileActions();
    protected final BrowserActions browserActions = new BrowserActions();
    protected static final ThreadLocal<Boolean> toExecute = ThreadLocal.withInitial(() -> true);

    protected void addStatementCounter(String methodName, List<Object> param){
        toExecuteChecker(methodName , param);
    }

    protected void toExecuteChecker(String methodName, List<Object> param){
        System.out.println(methodName);
        switch (methodName){
            case "ifNumberIsBiggerThanNumber":
                if((Integer) param.get(0)>(Integer) param.get(1)){
                    System.out.println("masuk");
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
                if(!mobileActions.verifyElementVisible((String) param.get(0))){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
            case "ifElementIsVisible":
                if(mobileActions.verifyElementVisible((String) param.get(0))){
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
int i =0;
    protected void print(){
        System.out.println("print :"+i);
        i++;
    }


}
