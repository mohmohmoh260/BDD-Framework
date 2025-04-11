package builds.utilities;

import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.testng.xml.internal.Parser;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract utility class for parsing TestNG XML files.
 * <p>
 * This class helps extract suite-level and test-level parameters,
 * test names, class names, and filter tests by name from a `testng.xml` file.
 * </p>
 */
public abstract class TestNGXmlParser {

    /**
     * Thread-safe container for global device parameters (suite-level parameters).
     */
    protected static final ThreadLocal<List<Map<String, String>>> globalDeviceParameter =
            ThreadLocal.withInitial(() -> new ArrayList<>(getGlobalParameters()));

    /**
     * Retrieves global parameters defined at the suite level in the TestNG XML file.
     *
     * @return a list of maps representing suite-level global parameters.
     */
    private static List<Map<String, String>> getGlobalParameters() {
        String testngXmlPath = "testng.xml";
        List<Map<String, String>> globalParametersList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(testngXmlPath);
            Parser parser = new Parser(fileInputStream);
            List<XmlSuite> suites = (List<XmlSuite>) parser.parse();

            for (XmlSuite suite : suites) {
                Map<String, String> globalParams = suite.getParameters();
                if (!globalParams.isEmpty()) {
                    globalParametersList.add(new LinkedHashMap<>(globalParams));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return globalParametersList;
    }

    /**
     * Filters test details from the TestNG XML based on the test name.
     * If no match is found, the program will exit with error message.
     *
     * @param testName the name of the test to filter.
     * @return a list of maps containing filtered test details.
     */
    protected List<Map<String, String>> filterXMLByTestName(String testName) {
        List<Map<String, String>> allTests = getXMLContent();
        List<Map<String, String>> filteredTests = filterByTestName(allTests, testName);

        if (filteredTests.isEmpty()) {
            System.err.println("Please check the test name input is exist in testng.xml test tag name attribute value");
            System.exit(1);
        }

        return filteredTests;
    }

    /**
     * Loads and parses all test content from the TestNG XML.
     *
     * @return a list of maps representing each test's metadata and parameters.
     */
    private List<Map<String, String>> getXMLContent() {
        String testngXmlPath = "testng.xml";
        List<Map<String, String>> testDetailsList = new ArrayList<>();

        try {
            testDetailsList = parseTestNGXml(testngXmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return testDetailsList;
    }

    /**
     * Filters a list of test detail maps by the provided test name.
     *
     * @param testDetailsList the list of test metadata.
     * @param testName        the name of the test to filter by.
     * @return a filtered list matching the specified test name.
     */
    private List<Map<String, String>> filterByTestName(List<Map<String, String>> testDetailsList, String testName) {
        return testDetailsList.stream()
                .filter(map -> testName.equals(map.get("TestName")))
                .collect(Collectors.toList());
    }

    /**
     * Parses the TestNG XML file to extract all suite and test metadata.
     *
     * @param testngXmlPath path to the `testng.xml` file.
     * @return a list of maps containing details such as:
     * suite name, test name, global and local parameters, and test classes.
     */
    private List<Map<String, String>> parseTestNGXml(String testngXmlPath) {
        List<Map<String, String>> testDetailsList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(testngXmlPath);
            Parser parser = new Parser(fileInputStream);
            List<XmlSuite> suites = (List<XmlSuite>) parser.parse();

            for (XmlSuite suite : suites) {
                Map<String, String> suiteParameters = suite.getParameters();

                for (XmlTest test : suite.getTests()) {
                    Map<String, String> testDetails = new LinkedHashMap<>();
                    testDetails.put("SuiteName", suite.getName());
                    testDetails.put("TestName", test.getName());

                    // Add suite-level parameters with "Global_" prefix
                    for (Map.Entry<String, String> entry : suiteParameters.entrySet()) {
                        testDetails.put("Global_" + entry.getKey(), entry.getValue());
                    }

                    // Add test-level parameters
                    Map<String, String> testParameters = test.getLocalParameters();
                    testDetails.putAll(testParameters);

                    // Add all class names as comma-separated string
                    List<String> classes = new ArrayList<>();
                    test.getXmlClasses().forEach(xmlClass -> classes.add(xmlClass.getName()));
                    testDetails.put("Classes", String.join(", ", classes));

                    testDetailsList.add(testDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testDetailsList;
    }
}