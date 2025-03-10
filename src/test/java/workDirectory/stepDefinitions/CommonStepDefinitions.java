package workDirectory.stepDefinitions;

import builds.snippetClasses.GherkinDataTableExtractor;
import builds.snippetClasses.GherkinStepRunner;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

public class CommonStepDefinitions extends CommonMethods{

    private final GherkinStepRunner stepRunner = new GherkinStepRunner(List.of(CommonStepDefinitions.class));
    private static final ThreadLocal<Set<String>> executingScenarios = ThreadLocal.withInitial(HashSet::new);

    // Method to handle snippet
    @When("run snippet scenario {string}")
    public void runSnippetScenario(String scenarioName) throws Exception {
        Set<String> runningScenarios = executingScenarios.get();

        // Prevent infinite recursion
        if (runningScenarios.contains(scenarioName)) {
            throw new IllegalStateException("Detected recursive scenario execution: " + scenarioName);
        }

        runningScenarios.add(scenarioName);

            List<String> steps = GherkinDataTableExtractor.getStepsFromScenario(scenarioName);
            if (steps.isEmpty()) {
                throw new IllegalArgumentException("Scenario not found: " + scenarioName);
            }

            for (String step : steps) {
                System.out.println("Executing scenario: " + step);
                DataTable dataTable = GherkinDataTableExtractor.getDataTableFromFeature(step);
                stepRunner.executeStep(step, dataTable); // Pass DataTable if found
            }

            runningScenarios.remove(scenarioName);

    }

    // Adding addition parameter for gherkin
    @ParameterType("true|false")
    public Boolean booleanType(String value) {
        return Boolean.parseBoolean(value);
    }

    @And ("if {int} is bigger than {int}")
    public void ifNumberIsBiggerThanNumber(int firstValue, int secondValue){
       addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Arrays.asList(firstValue,secondValue));
    }

    @And ("if {int} is smaller than {int}")
    public void ifNumberIsSmallerThanNumber(int firstValue, int secondValue){
        addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Arrays.asList(firstValue,secondValue));
    }

    @And ("if {string} is not visible")
    public void ifElementIsNotVisible(String elementName){
        addStatementCounter(new Object(){}.getClass().getEnclosingMethod().getName(), Collections.singletonList(elementName));
    }

    @And("end statement")
    public void endIfStatement(){
        endIf();
    }

    @Then("print from data table without header below")
    public void printFromDataTableWithoutHeaderBelow(DataTable dataTable) {
        System.out.println(dataTable.cells());
    }

    @Then("print from data table with header below")
    public void printFromDataTableWithHeaderBelow(DataTable dataTable) {
        System.out.println(dataTable.cells());
    }

    @Then("print string {string} {double} {booleanType}")
    public void printString(String arg0, Double arg1, Boolean arg2) {
        System.out.println(arg0);
        System.out.println(arg1);
        System.out.println(arg2);
    }

    @Given("I launch the Mobile Simulator {string}")
    public void iLaunchTheMobileSimulator(String variableURL) {
        if(toExecute.get()){
            startIOS(variableURL);
        }
    }

    @Given("I navigate mobile browser to {string}")
    public void iNavigateMobileBrowserTo(String arg0) {
        if(toExecute.get()) {
            mobileActions.navigateToURL(arg0);
        }
    }

    @Given("I navigate browser to {string}")
    public void iNavigateBrowserTo(String URL) {
        if(toExecute.get()) {
            browserActions.navigateToURL(URL);
        }
    }

    @When("I set text {string} into {string}")
    public void iSetTextInto(String value, String elementName) {
        if(toExecute.get()){
            setText(value, elementName);
        }
    }

    @And("take screenshot")
    public void takeAScreenshot() {
        if(toExecute.get()){
           takeScreenshot();
        }
    }

    @Given("I launch the browser and navigate to {string}")
    public void iLaunchTheBrowserAndNavigateTo(String credential) {
        if(toExecute.get()){
            browserActions.browserSetup(credential);
        }
    }

    @When("I click {string}")
    public void iClick(String elementName) {

    }
}
