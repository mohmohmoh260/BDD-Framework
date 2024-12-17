package builds.main;

import builds.utilities.TestNGXmlParser;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CucumberRun {
	@CucumberOptions(
			plugin= {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
			features= {"src/test/resources"},
			glue = {"workDirectory.stepDefinitions"},
			tags = ("@Test1")
	)

	public class TestRunner extends AbstractTestNGCucumberTests {
		@DataProvider(parallel = true)
		public Object[][] scenarios() {
			return super.scenarios();
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
	}
}
