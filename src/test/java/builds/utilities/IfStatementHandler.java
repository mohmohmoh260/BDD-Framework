package builds.utilities;

import builds.actions.MainActions;

import java.util.*;

public class IfStatementHandler extends MainActions {

    public void toExecuteChecker(String methodName, List<Object> param, Integer timeout){

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
                if(verifyElementNotVisible((String) param.get(0), timeout)){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
                break;
            case "ifElementIsVisible":
                if(verifyElementVisible((String) param.get(0), timeout)){
                    toExecute.set(true);
                }else {
                    toExecute.set(false);
                }
                break;
            default:
                System.out.println("if statement is not exist, please check if the method name is exist in toExecuteChecker method");
        }
    }

    public void endIf(){
       toExecute.set(true);
    }

}
