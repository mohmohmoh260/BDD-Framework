package workDirectory.stepDefinitions;

import builds.snippetClasses.GherkinDataTableExtractor;
import builds.snippetClasses.GherkinStepRunner;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

public class CommonStepDefinitions extends CommonMethods{

    private final GherkinStepRunner stepRunner = new GherkinStepRunner(List.of(CommonStepDefinitions.class));
    private static final ThreadLocal<Set<String>> executingScenarios = ThreadLocal.withInitial(HashSet::new);

    @When("run snippet scenario {string}")
    public void runSnippetScenario(String scenarioName) throws Exception {
        Set<String> runningScenarios = executingScenarios.get();

        // Prevent infinite recursion
        if (runningScenarios.contains(scenarioName)) {
            throw new IllegalStateException("Detected recursive scenario execution: " + scenarioName);
        }

        runningScenarios.add(scenarioName);
        try {
            List<String> steps = GherkinDataTableExtractor.getStepsFromScenario(scenarioName);
            if (steps.isEmpty()) {
                throw new IllegalArgumentException("Scenario not found: " + scenarioName);
            }

            System.out.println("Executing scenario: " + scenarioName);
            for (String step : steps) {
                DataTable dataTable = GherkinDataTableExtractor.getDataTableFromFeature(step);
                stepRunner.executeStep(step, dataTable); // Pass DataTable if found
            }
        } finally {
            runningScenarios.remove(scenarioName);
        }
    }

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

    @And("I launch the browser and navigate to Google page with {string}")
    public void i_launch_the_browser_and_navigate_to_google_page_with(String browserType) {
        if(toExecute.get()){
            browserActions.browserSetup(browserType);
        }
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
}
