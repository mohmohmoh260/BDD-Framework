package builds.main;

import builds.extent.ExtentManager;
import builds.snippet.FeatureDuplicateChecker;
import io.cucumber.testng.*;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Main runner class for executing Cucumber tests with TestNG.
 * <p>
 * Includes process cleanup, duplicate feature validation,
 * and auto-opening the report after execution.
 */
public class CucumberRun {

	/**
	 * Cucumber Test Runner class using TestNG.
	 * <p>
	 * Defines features, step definitions, tags, and report listeners.
	 */
	@CucumberOptions(
			plugin = {"builds.listener.StepListener"},
			features = {"src/test/resources/Features", "src/test/resources/Snippet"},
			glue = {"workDirectory.stepDefinitions", "builds.extent.ReportFinalizer"},
			tags = ("@test")
	)
	public static class TestRunner extends AbstractTestNGCucumberTests {

		/**
		 * Provides scenarios for parallel execution.
		 *
		 * @return Object[][] containing scenario data.
		 */
		@Override
		@DataProvider(parallel = true)
		public Object[][] scenarios() {
			return super.scenarios();
		}

		/**
		 * Kills existing ADB and ChromeDriver processes before test suite runs.
		 * Prevents port/resource conflicts during test execution.
		 */
		@BeforeSuite
		public void killProcesses() {
			String os = System.getProperty("os.name").toLowerCase();
			String[] command;

			if (os.contains("win")) {
				command = new String[]{"cmd.exe", "/c", "taskkill /F /IM adb.exe && taskkill /F /IM chromedriver.exe"};
			} else {
				command = new String[]{"/bin/bash", "-c", "killall adb; killall chromedriver"};
			}

			try {
				ProcessBuilder processBuilder = new ProcessBuilder(command);
				processBuilder.inheritIO();
				Process process = processBuilder.start();
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				System.err.println("Failed to kill processes.");
			}
		}

		/**
		 * Checks for duplicate scenario names in Snippet feature files.
		 * <p>
		 * Prevents ambiguity errors during runtime due to repeated scenario titles.
		 *
		 * @throws IOException if file reading fails.
		 */
		@BeforeSuite
		public void featuresChecker() throws IOException {
			String featureDirectory = "src/test/resources/Snippet";
			FeatureDuplicateChecker.findDuplicateScenarios(featureDirectory);
		}

		/**
		 * Automatically opens the generated Extent Report in the default browser after the test suite finishes.
		 *
		 * @throws IOException if report file cannot be opened.
		 */
		@AfterSuite
		public void navigateToReportFolder() throws IOException {
			String OS = System.getProperty("os.name");
			String reportPath = System.getProperty("user.dir") + (OS.contains("Mac OS") ? "/" : "\\") +
					ExtentManager.baseReportFolder + "ExtentReport.html";

			Desktop.getDesktop().open(new File(reportPath));
		}
	}
}