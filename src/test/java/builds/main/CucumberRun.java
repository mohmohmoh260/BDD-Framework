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

		static {
			File featureFolder = new File("src/test/resources/Snippet");
			System.out.println("🔎 Checking feature path: " + featureFolder.getAbsolutePath());

			if (!featureFolder.exists() || !featureFolder.isDirectory()) {
				System.err.println("❌ ERROR: Feature directory not found!");
			} else {
				System.out.println("✅ Feature directory found. Listing files:");
				Arrays.stream(featureFolder.listFiles()).forEach(file ->
						System.out.println("📂 Found: " + file.getName())
				);
			}
			System.out.println("📌 Current classpath: " + System.getProperty("java.class.path"));
		}

		@Override
		@DataProvider(parallel = false)
		public Object[][] scenarios() {
			return super.scenarios();
		}

		public void runScenarioByName(String scenarioName) {
			System.out.println("🚀 Searching for scenario: " + scenarioName);

			Object[][] allScenarios = scenarios();  // Get all available scenarios

			for (Object[] scenarioData : allScenarios) {
				PickleWrapper pickleWrapper = (PickleWrapper) scenarioData[0];
				FeatureWrapper featureWrapper = (FeatureWrapper) scenarioData[1];

				if (pickleWrapper.getPickle().getName().equalsIgnoreCase(scenarioName)) {
					System.out.println("✅ Found & Running: " + scenarioName);
					runScenario(pickleWrapper, featureWrapper);
					return;
				}
			}
			System.err.println("❌ Scenario Not Found: " + scenarioName);
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
