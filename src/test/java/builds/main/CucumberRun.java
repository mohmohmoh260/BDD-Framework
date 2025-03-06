package builds.main;

import builds.utilities.BrowserInstance;
import builds.utilities.ElementInstance;
import builds.utilities.MobileInstance;
import io.cucumber.core.cli.Main;
import io.cucumber.testng.*;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CucumberRun {
	@CucumberOptions(
			plugin= {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "builds.utilities.StepListener"},
			features= {"src/test/resources/Features", "src/test/resources/Snippet"},
			glue = {"workDirectory.stepDefinitions"},
			tags = ("@test")
	)

	public static class TestRunner extends AbstractTestNGCucumberTests {

		@Override
		@DataProvider(parallel = false)
		public Object[][] scenarios() {
			return super.scenarios();
		}

		@AfterSuite @BeforeSuite
		public void clearDriverInstance(){
			MobileInstance mobileInstance = new MobileInstance();
			mobileInstance.quitMobileDriver();

			BrowserInstance browserInstance = new BrowserInstance();
			browserInstance.quitWebDriver();
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

		@BeforeSuite
		public void fetchAllElements(){
			ElementInstance.getAllElement();
		}
	}
}
