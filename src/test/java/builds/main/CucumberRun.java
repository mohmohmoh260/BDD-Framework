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
			tags = ("@Regression")
	)

	public static class TestRunner extends AbstractTestNGCucumberTests {
		private static final Logger logger = null;

		@BeforeAll
		public static void beforeAll() {
			// To Kill all Driver Before Starting
			String[] drivers = {"chromedriver", "geckodriver", "safaridriver"};

			for (String driver : drivers) {
				try {
					// Execute the kill command
					Process process = Runtime.getRuntime().exec(new String[] { "sudo", "pkill", "-f", driver });

					// Optionally, capture and print the output (useful for debugging)
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(process.getInputStream())
					);
					String line;
					while ((line = reader.readLine()) != null) {
						logger.info(line);
					}

					process.waitFor(); // Wait for the command to complete
					logger.info("Killed all instances of: " + driver);
				} catch (Exception e) {
					logger.warning("Failed to kill driver: " + driver);
					e.printStackTrace();
				}
			}
		}

		@BeforeSuite
		public void loadingGlobalVariable() throws IOException {
			// To Load Global Variable.properties file
			GlobalProperties.loadGlobalVariablesProperties();
		}

		@DataProvider(parallel = true)
		@Override
		public Object[][] scenarios(){
			return super.scenarios();
		}

		@AfterSuite
		public void afterAll() throws IOException {
			String OS = System.getProperty("os.name");

			if(!OS.contains("Mac OS")){
				Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"\\Test Reports"));
			}else {
				Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"/Test Reports"));
			}
		}
	}
}
