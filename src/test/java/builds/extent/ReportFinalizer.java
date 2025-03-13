package builds.extent;

import org.testng.annotations.AfterSuite;
import java.io.File;

public class ReportFinalizer {

    @AfterSuite
    public void afterExtentReportGenerated() {
        String reportPath = "test-output/ExtentReport.html";

        File reportFile = new File(reportPath);

        // Small delay to ensure the report is written to disk
        try {
            Thread.sleep(5000); // Wait for 5 seconds (optional)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (reportFile.exists()) {
            System.out.println("✅ HTML Report generated successfully: " + reportFile.getAbsolutePath());
            // Additional actions (send email, upload, etc.)
        } else {
            System.err.println("❌ HTML Report was not found! Check Extent Report generation.");
        }
    }
}