package builds.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static final ExtentReports extent = new ExtentReports();

    public static ExtentReports createInstance() {
        String reportPath = "test-output/ExtentReport.html";
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);

        // Configure the report layout (optional)
        htmlReporter.viewConfigurer().viewOrder().as(new com.aventstack.extentreports.reporter.configuration.ViewName[]{
                com.aventstack.extentreports.reporter.configuration.ViewName.DASHBOARD,
                com.aventstack.extentreports.reporter.configuration.ViewName.TEST,
                com.aventstack.extentreports.reporter.configuration.ViewName.EXCEPTION
        }).apply();

        extent.attachReporter(htmlReporter);
        return extent;
    }
}