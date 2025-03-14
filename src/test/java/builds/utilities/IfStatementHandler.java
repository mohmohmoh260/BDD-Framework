package builds.utilities;

import builds.actions.BrowserActions;
import builds.actions.MainActions;
import builds.actions.MobileActions;

import java.util.*;

public class IfStatementHandler extends MainActions {

    private final MobileActions mobileActions = new MobileActions();
    private final BrowserActions browserActions = new BrowserActions();

    public void toExecuteChecker(String methodName, List<Object> param, Integer timeout){
        Result result;
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
                result = verifyElementVisible((String) param.get(0), timeout);
                if(!result.success){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
            case "ifElementIsVisible":
                result = verifyElementVisible((String) param.get(0), timeout);
                if(result.success){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
            default:
                System.out.println("if statement is not exist, please check if the method name is exist in toExecuteChecker method");
        }
    }

    public void endIf(){
       toExecute.set(true);
    }

}
