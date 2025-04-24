package workDirectory.stepDefinitions;

import builds.actions.MainActions;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class IfStatementStepDefinitions extends MainActions {

    /**
     * Executes conditional logic if an element is not visible within the given timeout.
     *
     * @param elementName The name of the element to check
     * @param timeout Optional timeout in seconds
     */
    @And("^if element \"([^\"]+)\" is not visible(?: within (\\d+) seconds)?$")
    public void ifElementIsNotVisible(String elementName, Integer timeout){
        if(toExecute.get().getLast()){
            if(verifyElementNotVisible(elementName, timeout)){
                toExecute.get().add(true);
            }else{
                toExecute.get().add(false);
            }
        }
    }

    /**
     * Executes conditional logic if an element is visible within the given timeout.
     *
     * @param elementName The name of the element to check
     * @param timeout Optional timeout in seconds
     */
    @And ("^if element \"([^\"]+)\" is visible(?: within (\\d+) seconds)?$")
    public void ifElementIsVisible(String elementName, Integer timeout){
        if(toExecute.get().getLast()){
            if(verifyElementVisible(elementName, timeout)){
                toExecute.get().add(true);
            }else{
                toExecute.get().add(false);
            }
        }
    }

    /**
     * Executes conditional logic if a text is not visible within the given timeout.
     *
     * @param text The text to check
     * @param timeout Optional timeout in seconds
     */
    @And("^if text \"([^\"]+)\" is not visible(?: within (\\d+) seconds)?$")
    public void ifTextIsNotVisible(String text, Integer timeout){
        if(toExecute.get().getLast()){
            if(verifyTextNotVisible(text, timeout)){
                toExecute.get().add(true);
            }else{
                toExecute.get().add(false);
            }
        }
    }

    /**
     * Executes conditional logic if a text is not visible within the given timeout.
     *
     * @param text The text to check
     * @param timeout Optional timeout in seconds
     */
    @And("^if text \"([^\"]+)\" is visible(?: within (\\d+) seconds)?$")
    public void ifTextIsVisible(String text, Integer timeout){
        if(toExecute.get().getLast()){
            if(verifyTextVisible(text, timeout)){
                toExecute.get().add(true);
            }else{
                toExecute.get().add(false);
            }
        }
    }

    /**
     * Ends an "if" statement block.
     */
    @And("end if statement")
    public void endIfStatement(){
        try{
            toExecute.get().removeLast();
            writeReportPassed();
        }catch (Throwable t){
            writeReportFailed(t);
        }

    }

    @When("^if (\\d+) is smaller than (\\d+)$")
    public void ifIsSmallerThan(int number1, int number2) {
        if(number1<number2){
            toExecute.get().add(true);
        }else {
            toExecute.get().add(false);
        }
    }
}
