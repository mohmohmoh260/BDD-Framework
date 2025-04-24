package builds.snippet;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.*;

/**
 * Responsible for dynamically executing step definitions based on a Gherkin step string.
 * Uses reflection to find and invoke matching methods annotated with Cucumber step annotations.
 */
public class GherkinStepRunner {

    private static List<Class<?>> stepDefinitionClasses = null;

    /**
     * Constructor to initialize the runner with a list of step definition classes.
     *
     * @param stepDefinitionClasses a list of classes containing Cucumber step definitions
     */
    public GherkinStepRunner(List<Class<?>> stepDefinitionClasses) {
        GherkinStepRunner.stepDefinitionClasses = stepDefinitionClasses;
    }

    /**
     * Executes a Gherkin step by matching it against known step definitions.
     *
     * @param gherkinStep the Gherkin step text (e.g., "Given user is on login page")
     * @param dataTable   optional Cucumber {@link DataTable} to be passed to the step definition
     * @throws Throwable if the invoked method throws an exception or no match is found
     */
    public void executeStep(String gherkinStep, DataTable dataTable) throws Throwable {
        String cleanedStep = gherkinStep.replaceFirst("^(Given|When|Then|And|\\$)\\s+", "").trim();

        for (Class<?> clazz : stepDefinitionClasses) {
            for (Method method : clazz.getDeclaredMethods()) {
                String annotationValue = getCucumberAnnotationValue(method);
                if (annotationValue == null) continue;

                Pattern pattern = buildRegexPattern(annotationValue);
                Matcher matcher = pattern.matcher(cleanedStep);

                if (matcher.matches()) {
                    Object instance = clazz.getDeclaredConstructor().newInstance();

                    // Extract parameters
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] params = new Object[matcher.groupCount()];

                    for (int i = 0; i < matcher.groupCount(); i++) {
                        params[i] = convertParameter(parameterTypes[i], matcher.group(i + 1));
                    }

                    // Handle DataTable if required
                    if (parameterTypes.length == params.length + 1 && parameterTypes[params.length].equals(DataTable.class)) {
                        params = appendDataTable(params, dataTable != null ? dataTable : DataTable.create(Collections.emptyList()));
                    }

                    // Invoke the method and handle exceptions
                    try {
//                        System.out.println("Executing: "+gherkinStep);
                        method.invoke(instance, params);
                        return;  // Step executed successfully
                    } catch (InvocationTargetException e) {
                        throw e.getCause();  // Rethrow actual exception from step definition
                    } catch (Exception e) {
                        e.printStackTrace(); // Log unexpected exceptions
                        return;
                    }
                }
            }
        }
        throw new NoSuchMethodException("❌ No matching step definition found for: " + gherkinStep);
    }

    /**
     * Extracts the value of any Cucumber step annotation present on the method.
     *
     * @param method the method to check
     * @return the annotation value if found, null otherwise
     */
    private String getCucumberAnnotationValue(Method method) {
        if (method.isAnnotationPresent(Given.class)) return method.getAnnotation(Given.class).value();
        if (method.isAnnotationPresent(When.class)) return method.getAnnotation(When.class).value();
        if (method.isAnnotationPresent(Then.class)) return method.getAnnotation(Then.class).value();
        if (method.isAnnotationPresent(And.class)) return method.getAnnotation(And.class).value();
        return null;
    }

    /**
     * Converts a Cucumber expression or regex into a regular expression {@link Pattern}.
     * Supports common placeholder replacements (e.g., booleans and numbers).
     *
     * @param stepDefinition the annotated step string from the step definition
     * @return compiled regex {@link Pattern}
     */
    private Pattern buildRegexPattern(String stepDefinition) {
        String regex = stepDefinition
                .replaceAll("\\{boolean\\}", "(true|false)") // Matches booleans
                .replaceAll("\\(\\\\d\\+\\)", "(\\\\d+)");    // Matches (\\d+)
        return Pattern.compile("^" + regex + "$");
    }

    /**
     * Converts a matched regex value into its expected Java parameter type.
     *
     * @param type the expected parameter type
     * @param value the string value extracted from the step
     * @return the converted object
     */
    private Object convertParameter(Class<?> type, String value) {
        if (value == null) {
            if (type.equals(int.class) || type.equals(double.class) || type.equals(boolean.class)) {
                throw new IllegalArgumentException("❌ Cannot convert null to a primitive type: " + type.getSimpleName());
            }
            return null;
        }

        if (type.equals(int.class) || type.equals(Integer.class)) return Integer.parseInt(value);
        if (type.equals(double.class) || type.equals(Double.class)) return Double.parseDouble(value);
        if (type.equals(boolean.class) || type.equals(Boolean.class)) return Boolean.parseBoolean(value);

        return value;
    }

    /**
     * Appends a {@link DataTable} to an existing array of method parameters.
     *
     * @param original the original parameters
     * @param dataTable the data table to append
     * @return a new parameter array including the {@link DataTable}
     */
    private Object[] appendDataTable(Object[] original, DataTable dataTable) {
        Object[] newArray = new Object[original.length + 1];
        System.arraycopy(original, 0, newArray, 0, original.length);
        newArray[original.length] = dataTable;
        return newArray;
    }
}