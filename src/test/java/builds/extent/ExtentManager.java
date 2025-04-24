package builds.extent;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExtentManager {

    private static final ExtentReports extentReports = new ExtentReports();
    private static final ThreadLocal<ExtentTest> extent = new ThreadLocal<>();
    private static final ThreadLocal<Deque<ExtentTest>> nodeStack = ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<List<LogEntry>> bufferedLogs = ThreadLocal.withInitial(ArrayList::new);

    public static final String baseReportFolder;
    public static final String baseScreenshotFolder;

    static {
        String timeStamp = new SimpleDateFormat("dd.MM.yy_HH-mm-ss").format(new Date());
        String os = System.getProperty("os.name");

        if (os.contains("Mac OS")) {
            baseReportFolder = "test-output/" + timeStamp + "/";
            baseScreenshotFolder = baseReportFolder + "screenshot/";
        } else {
            baseReportFolder = "test-output\\" + timeStamp + "\\";
            baseScreenshotFolder = baseReportFolder + "screenshot\\";
        }

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

    public static synchronized ExtentReports getInstance() {
        return extentReports;
    }

    public static synchronized ExtentTest getExtent() {
        return extent.get();
    }

    public static synchronized void setExtent(ExtentTest extentTest) {
        extent.set(extentTest);
        nodeStack.get().clear();
        nodeStack.get().push(extentTest);  // Push root node
    }

    public static synchronized Deque<ExtentTest> getNodeStack(){
        return nodeStack.get();
    }

    public static synchronized void createAndPushNode(String nodeName) {
        ExtentManager.flushBufferedLogsToExtent();
        ExtentTest parent = nodeStack.get().peek();
        ExtentTest child = parent.createNode(nodeName);
        nodeStack.get().push(child);
    }

    public static synchronized void popNode() {
        ExtentManager.flushBufferedLogsToExtent();
        if (!nodeStack.get().isEmpty()) {
            nodeStack.get().pop();
        }
    }

    public static synchronized void flush() {
        extentReports.flush();
    }

    private static class LogEntry {
        Status status;
        String message;
        Media media;

        LogEntry(Status status, String message, Media media) {
            this.status = status;
            this.message = message;
            this.media = media;
        }
    }

    public static void bufferLog(Status status, String message, Media media) {
        bufferedLogs.get().add(new LogEntry(status, message, media));
    }

    private static void flushBufferedLogsToExtent() {
        if (bufferedLogs.get().isEmpty()) return;

        ExtentTest current = nodeStack.get().peek();
        for (LogEntry log : bufferedLogs.get()) {
            if (log.media != null) {
                current.log(log.status, log.message, log.media);
            } else {
                current.log(log.status, log.message);
            }
        }

        bufferedLogs.get().clear();
    }
}