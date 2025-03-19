package builds.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.text.SimpleDateFormat;

public class ExtentManager {

    private static final ThreadLocal<ExtentTest> extent = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> nodeExtent = new ThreadLocal<>();
    private static final ExtentReports extentReports = new ExtentReports();
    public static final String baseReportFolder;
    public static final String baseScreenshotFolder;

    static {
        String timeStamp = new SimpleDateFormat("dd.MM.yy_HH-mm-ss").format(new java.util.Date());
        String OS = System.getProperty("os.name");

        if(!OS.contains("Mac OS")){
            baseReportFolder = "test-output\\" + timeStamp + "\\";
            baseScreenshotFolder = baseReportFolder + "\\screenshot\\";
        }else {
            baseReportFolder = "test-output/" + timeStamp + "/";
            baseScreenshotFolder = baseReportFolder + "/screenshot/";
        }

        com.aventstack.extentreports.reporter.ExtentSparkReporter spark = new ExtentSparkReporter(baseReportFolder + "ExtentReport.html").viewConfigurer()
        .viewOrder()
        .as(new ViewName[] {
                ViewName.DASHBOARD,
                ViewName.TEST,
                ViewName.CATEGORY,
                ViewName.AUTHOR,
                ViewName.DEVICE,
                ViewName.EXCEPTION,
                ViewName.LOG
        })
        .apply();
         spark.config(
        ExtentSparkReporterConfig.builder()
                .theme(Theme.STANDARD)
                .documentTitle("MyReport")
                .build()
        );
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