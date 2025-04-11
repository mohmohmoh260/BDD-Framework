package builds.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.text.SimpleDateFormat;

/**
 * ExtentManager is responsible for initializing and managing ExtentReports instances.
 * It handles thread-safe creation of test and node logs for parallel test execution.
 */
public class ExtentManager {

    // Thread-local ExtentTest for individual test cases
    private static final ThreadLocal<ExtentTest> extent = new ThreadLocal<>();

    // Thread-local ExtentTest for node-level logs (e.g., steps under a test)
    private static final ThreadLocal<ExtentTest> nodeExtent = new ThreadLocal<>();

    // Singleton ExtentReports instance
    private static final ExtentReports extentReports = new ExtentReports();

    // Report output directory and screenshot directory (OS-aware)
    public static final String baseReportFolder;
    public static final String baseScreenshotFolder;

    // Static block to initialize folders and configure ExtentSparkReporter
    static {
        String timeStamp = new SimpleDateFormat("dd.MM.yy_HH-mm-ss").format(new java.util.Date());
        String OS = System.getProperty("os.name");

        if (!OS.contains("Mac OS")) {
            baseReportFolder = "test-output\\" + timeStamp + "\\";
            baseScreenshotFolder = baseReportFolder + "\\screenshot\\";
        } else {
            baseReportFolder = "test-output/" + timeStamp + "/";
            baseScreenshotFolder = baseReportFolder + "/screenshot/";
        }

        // Initialize ExtentSparkReporter with desired configuration and view layout
        ExtentSparkReporter spark = new ExtentSparkReporter(baseReportFolder + "ExtentReport.html")
                .viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.CATEGORY,
                        ViewName.AUTHOR,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.LOG
                }).apply();

        spark.config(
                ExtentSparkReporterConfig.builder()
                        .theme(Theme.STANDARD)
                        .documentTitle("MyReport")
                        .build()
        );

        extentReports.attachReporter(spark);
    }

    /**
     * Gets the thread-local ExtentTest instance for the current test.
     *
     * @return ExtentTest instance for the current thread
     */
    public static synchronized ExtentTest getExtent() {
        return extent.get();
    }

    /**
     * Sets the thread-local ExtentTest instance for the current test.
     *
     * @param test ExtentTest to be associated with the current thread
     */
    public static synchronized void setExtent(ExtentTest test) {
        extent.set(test);
    }

    /**
     * Removes the ExtentTest instance from the current thread to prevent memory leaks.
     */
    public static synchronized void removeExtent() {
        extent.remove();
    }

    /**
     * Gets the thread-local node ExtentTest instance.
     * Typically used for logging sub-steps under a main test case.
     *
     * @return ExtentTest instance representing a node
     */
    public static synchronized ExtentTest getNodeExtent() {
        return nodeExtent.get();
    }

    /**
     * Sets the node ExtentTest instance for the current thread.
     *
     * @param test Node-level ExtentTest
     */
    public static synchronized void setNodeExtent(ExtentTest test) {
        nodeExtent.set(test);
    }

    /**
     * Removes the node ExtentTest instance from the current thread.
     */
    public static synchronized void removeNodeExtent() {
        nodeExtent.remove();
    }

    /**
     * Returns the singleton ExtentReports instance.
     *
     * @return ExtentReports
     */
    public static synchronized ExtentReports getInstance() {
        return extentReports;
    }

    /**
     * Flushes all logs to the HTML report file.
     * This should be called once after test execution is complete.
     */
    public static synchronized void flush() {
        extentReports.flush();
    }
}