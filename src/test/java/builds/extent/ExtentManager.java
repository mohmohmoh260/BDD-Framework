package builds.extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.service.ExtentService;

public class ExtentManager {

    private static final ThreadLocal<ExtentReports > extent = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent.get() == null) {
            extent.set(ExtentService.getInstance()); // Load from extent.properties
        }
        return extent.get();
    }
}