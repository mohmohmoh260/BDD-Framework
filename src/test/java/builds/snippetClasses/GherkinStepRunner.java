package builds.snippetClasses;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GherkinStepRunner {
    private final List<Class<?>> stepDefinitionClasses;

    public GherkinStepRunner(List<Class<?>> stepDefinitionClasses) {
        this.stepDefinitionClasses = stepDefinitionClasses;
    }

    public void executeStep(String gherkinStep, DataTable dataTable) throws Exception {
        String cleanedStep = gherkinStep.replaceFirst("^(Given|When|Then|And)\\s+", "").trim();

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
                    method.invoke(instance, params);
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
                .replaceAll("\\{string\\}", "\"([^\"]*)\"")   // Matches quoted strings
                .replaceAll("\\{int\\}", "(-?\\\\d+)")          // Matches integers (positive/negative)
                .replaceAll("\\{double\\}", "(-?\\\\d+\\\\.\\\\d+)") // Matches doubles// Matches floats
                .replaceAll("\\{booleanType\\}", "(true|false)")  // Matches booleans
                + "$"; // Ensure full match
        return Pattern.compile("^" + regex);
    }

    private Object convertParameter(Class<?> type, String value) {
        if (type.equals(int.class) || type.equals(Integer.class)) return Integer.parseInt(value);
        if (type.equals(double.class) || type.equals(Double.class)) return Double.parseDouble(value);
        if (type.equals(float.class) || type.equals(Float.class)) return Float.parseFloat(value);
        if (type.equals(boolean.class) || type.equals(Boolean.class)) return Boolean.parseBoolean(value);
        return value; // Default to string
    }

    private Object[] appendDataTable(Object[] original, DataTable dataTable) {
        Object[] newArray = new Object[original.length + 1];
        System.arraycopy(original, 0, newArray, 0, original.length);
        newArray[original.length] = dataTable;
        return newArray;
    }
}