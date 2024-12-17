package builds.utilities;

import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.testng.xml.internal.Parser;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class TestNGXmlParser {

    public List<Map<String, String>> filterXMLByTestName(String testName){
        List<Map<String, String>> allTests = getXMLContent();

        // Example usage: filter by test name
        List<Map<String, String>> filteredTests = filterByTestName(allTests, testName);

//        System.out.println("Filtered Tests for Test Name: " + testName);
//        for (Map<String, String> testDetails : filteredTests) {
//            System.out.println("========================================");
//            for (Map.Entry<String, String> entry : testDetails.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
//        }
        return filteredTests;
    }

    public List<Map<String, String>> getGlobalParametersOnly() {
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
                    Map<String, String> globalParamsMap = new LinkedHashMap<>();
                    // Add suite-level parameters
                    for (Map.Entry<String, String> entry : globalParams.entrySet()) {
                        globalParamsMap.put(entry.getKey(), entry.getValue());
                    }

                    // Add global parameters to the list
                    globalParametersList.add(globalParamsMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return globalParametersList;
    }

    private List<Map<String, String>> getXMLContent() {
        String testngXmlPath = "testng.xml"; // Replace with the path to your TestNG XML file
        List<Map<String, String>> testDetailsList = new ArrayList<>();

        try {
            testDetailsList = parseTestNGXml(testngXmlPath);

//            System.out.println("Extracted TestNG XML Details:");
//            for (Map<String, String> testDetails : testDetailsList) {
//                System.out.println("========================================");
//                for (Map.Entry<String, String> entry : testDetails.entrySet()) {
//                    System.out.println(entry.getKey() + ": " + entry.getValue());
//                }
//            }
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
                    for (Map.Entry<String, String> entry : testParameters.entrySet()) {
                        testDetails.put(entry.getKey(), entry.getValue());
                    }

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
