package builds.main;

import builds.extent.ExtentManager;
import builds.utilities.BrowserInstance;
import builds.utilities.ElementInstance;
import builds.utilities.MobileInstance;
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

		private static final ExtentReports extent = ExtentManager.createInstance();

		@Override
		@DataProvider(parallel = false)
		public Object[][] scenarios() {
			return super.scenarios();
		}

		@BeforeSuite
		public void clearDriverInstance(){
			MobileInstance mobileInstance = new MobileInstance();
			mobileInstance.quitMobileDriver();

			BrowserInstance browserInstance = new BrowserInstance();
			browserInstance.quitWebDriver();
		}

		@BeforeSuite
		public void fetchData(){
			ElementInstance elementInstance = new ElementInstance();
			elementInstance.getAllElement();
		}

		@AfterSuite
		public void afterSuite() throws IOException {
			String OS = System.getProperty("os.name");

			if(!OS.contains("Mac OS")){
				Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"\\Test Reports"));
			}else {
				Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"/Test Reports"));
			}
		}

		@AfterSuite
		public void tearDown() {
			extent.flush();  // Ensure the report is finalized
		}

	}
}
