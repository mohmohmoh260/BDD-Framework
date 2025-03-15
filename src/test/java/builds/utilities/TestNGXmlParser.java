package builds.utilities;

import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.testng.xml.internal.Parser;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TestNGXmlParser {

    protected static final ThreadLocal<List<Map<String, String>>> globalDeviceParameter = ThreadLocal.withInitial(() -> new ArrayList<>(TestNGXmlParser.getGlobalParameters()));

    protected static List<Map<String, String>> getGlobalParameters() {
        String testngXmlPath = "testng.xml";
        List<Map<String, String>> globalParametersList = new ArrayList<>();
        try {
            // Parse the TestNG XML
            FileInputStream fileInputStream = new FileInputStream(testngXmlPath);
            Parser parser = new Parser(fileInputStream);
            List<XmlSuite> suites = (List<XmlSuite>) parser.parse();

            // Extract global parameters (suite level)
            for (XmlSuite suite : suites) {
                Map<String, String> globalParams = suite.getParameters();

                if (!globalParams.isEmpty()) {
                    // Add suite-level parameters
                    Map<String, String> globalParamsMap = new LinkedHashMap<>(globalParams);

                    // Add global parameters to the list
                    globalParametersList.add(globalParamsMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return globalParametersList;
    }

    protected List<Map<String, String>> filterXMLByTestName(String testName){
        List<Map<String, String>> allTests = getXMLContent();
        // Example usage: filter by test name
        List<Map<String, String>> filteredTests = filterByTestName(allTests, testName);
        if(filteredTests.isEmpty()){
            System.err.println("Please check the test name input is exist in testNG test tag name attribute value");
            System.exit(1);
        }
        return filteredTests;
    }

    private List<Map<String, String>> getXMLContent() {
        String testngXmlPath = "testng.xml"; // Replace with the path to your TestNG XML file
        List<Map<String, String>> testDetailsList = new ArrayList<>();

        try {
            testDetailsList = parseTestNGXml(testngXmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return testDetailsList;
    }

    private List<Map<String, String>> filterByTestName(List<Map<String, String>> testDetailsList, String testName) {
        return testDetailsList.stream()
                .filter(map -> testName.equals(map.get("TestName"))) // Check if "TestName" matches
                .collect(Collectors.toList());
    }

    private List<Map<String, String>> parseTestNGXml(String testngXmlPath) {
        List<Map<String, String>> testDetailsList = new ArrayList<>();
        try {
            // Parse the testng.xml
            FileInputStream fileInputStream = new FileInputStream(testngXmlPath);
            Parser parser = new Parser(fileInputStream);
            List<XmlSuite> suites = (List<XmlSuite>) parser.parse();

            for (XmlSuite suite : suites) {
                // Get global parameters (suite level)
                Map<String, String> suiteParameters = suite.getParameters();

                // Iterate over each test in the suite
                for (XmlTest test : suite.getTests()) {
                    Map<String, String> testDetails = new LinkedHashMap<>();

                    // Add suite name and test name
                    testDetails.put("SuiteName", suite.getName());
                    testDetails.put("TestName", test.getName());

                    // Add suite-level parameters
                    for (Map.Entry<String, String> entry : suiteParameters.entrySet()) {
                        testDetails.put("Global_" + entry.getKey(), entry.getValue());
                    }

                    // Add test-level parameters
                    Map<String, String> testParameters = test.getLocalParameters();
                    testDetails.putAll(testParameters);

                    // Add all class names under this test
                    List<String> classes = new ArrayList<>();
                    test.getXmlClasses().forEach(xmlClass -> classes.add(xmlClass.getName()));
                    testDetails.put("Classes", String.join(", ", classes));

                    // Store in the list
                    testDetailsList.add(testDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testDetailsList;
    }
}
