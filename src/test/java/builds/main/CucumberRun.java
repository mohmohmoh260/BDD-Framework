package builds.main;

import builds.utilities.GlobalProperties;
import io.cucumber.java.BeforeAll;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class CucumberRun {

	@CucumberOptions(
			plugin= {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
			features= {"src/test/resources"},
			glue = {"workDirectory.stepDefinitions"},
			tags = ("@Test2")
	)

	public class TestRunner extends AbstractTestNGCucumberTests {

		@BeforeSuite
		public void beforeSuite() {

		}

		@BeforeSuite
		public void loadingGlobalVariable() throws IOException {
			// To Load Global Variable.properties file
			GlobalProperties.loadGlobalVariablesProperties();
		}

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
