package builds.snippet;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.*;

public class GherkinStepRunner {

    private static List<Class<?>> stepDefinitionClasses = null;

    public GherkinStepRunner(List<Class<?>> stepDefinitionClasses) {
        GherkinStepRunner.stepDefinitionClasses = stepDefinitionClasses;
    }

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

                    // Invoke the method
                    try {
                        method.invoke(instance, params);
                    } catch (InvocationTargetException e) {
                        return;
                    }
                    return;
                }
            }
        }
        throw new NoSuchMethodException("No matching step definition found for: " + gherkinStep);
    }

    private String getCucumberAnnotationValue(Method method) {
        if (method.isAnnotationPresent(Given.class)) return method.getAnnotation(Given.class).value();
        if (method.isAnnotationPresent(When.class)) return method.getAnnotation(When.class).value();
        if (method.isAnnotationPresent(Then.class)) return method.getAnnotation(Then.class).value();
        if (method.isAnnotationPresent(And.class)) return method.getAnnotation(And.class).value();
        return null;
    }

    private Pattern buildRegexPattern(String stepDefinition) {
        String regex = stepDefinition
                .replaceAll("\\{boolean\\}", "(true|false)") // Matches booleans
                .replaceAll("\\(\\\\d\\+\\)", "(\\\\d+)");    // Matches (\\d+)
        return Pattern.compile("^" + regex + "$");
    }

    private Object convertParameter(Class<?> type, String value) {
        if (value == null) {
            if (type.equals(int.class) || type.equals(double.class) || type.equals(boolean.class)) {
                throw new IllegalArgumentException("❌ Cannot convert null to a primitive type: " + type.getSimpleName());
            }
            return null; // Return null for wrapper types and String
        }

        if (type.equals(int.class) || type.equals(Integer.class)) return Integer.parseInt(value);
        if (type.equals(double.class) || type.equals(Double.class)) return Double.parseDouble(value);
        if (type.equals(boolean.class) || type.equals(Boolean.class)) return Boolean.parseBoolean(value);

        return value;
    }

    private Object[] appendDataTable(Object[] original, DataTable dataTable) {
        Object[] newArray = new Object[original.length + 1];
        System.arraycopy(original, 0, newArray, 0, original.length);
        newArray[original.length] = dataTable;
        return newArray;
    }
}
