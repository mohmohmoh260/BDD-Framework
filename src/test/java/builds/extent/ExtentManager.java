package builds.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static final ThreadLocal<ExtentTest> extent = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> nodeExtent = new ThreadLocal<>();
    private static final ExtentReports extentReports = new ExtentReports();

    static {
        com.aventstack.extentreports.reporter.ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        extentReports.attachReporter(spark);
    }

    public static synchronized ExtentTest getExtent() {
        return extent.get();
    }

    public static synchronized void setExtent(ExtentTest test) {
        extent.set(test);
    }

    public static synchronized void removeExtent(){
        extent.remove();
    }

    public static synchronized ExtentTest getNodeExtent() {
        return nodeExtent.get();
    }

    public static synchronized void setNodeExtent(ExtentTest test) {
        nodeExtent.set(test);
    }

    public static synchronized void removeNodeExtent(){
        nodeExtent.remove();
    }

    public static synchronized ExtentReports getInstance() {
        return extentReports;
    }

    public static synchronized void flush() {
        extentReports.flush();
    }
}