package builds.main;

import builds.extent.ExtentManager;
import builds.driver.BrowserDriver;
import builds.elements.ElementInstance;
import builds.driver.MobileDriver;
import com.aventstack.extentreports.ExtentReports;
import io.cucumber.testng.*;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CucumberRun {
	@CucumberOptions(
			plugin= {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "builds.utilities.StepListener"},
			features= {"src/test/resources/Features", "src/test/resources/Snippet"},
			glue = {"workDirectory.stepDefinitions", "builds.extent.ReportFinalizer"},
			tags = ("@test")
	)

	public static class TestRunner extends AbstractTestNGCucumberTests {

		@Override
		@DataProvider(parallel = true)
		public Object[][] scenarios() {
			return super.scenarios();
		}

		@AfterSuite
		public void navigateToReportFolder() throws IOException {
			String OS = System.getProperty("os.name");

			if(!OS.contains("Mac OS")){
				Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"\\Test Reports"));
			}else {
				Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"/Test Reports"));
			}
		}
	}
}
