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

public class CucumberRun {
	@CucumberOptions(
			plugin= {"builds.listener.StepListener"},
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

		@BeforeSuite
		public static void killProcesses() {
			String os = System.getProperty("os.name").toLowerCase();

			String[] command;
			if (os.contains("win")) {
				// Windows commands
				command = new String[]{"cmd.exe", "/c", "taskkill /F /IM adb.exe && taskkill /F /IM chromedriver.exe"};
			} else {
				// Mac/Linux commands
				command = new String[]{"/bin/bash", "-c", "killall adb; killall chromedriver"};
			}

			try {
				ProcessBuilder processBuilder = new ProcessBuilder(command);
				processBuilder.inheritIO(); // Redirects output to the console
				Process process = processBuilder.start();
				process.waitFor(); // Waits for command execution to complete
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				System.err.println("Failed to kill processes.");
			}
		}

		@BeforeSuite
		public static void featuresChecker() throws IOException {
			String featureDirectory = "src/test/resources/Snippet";
			FeatureDuplicateChecker.findDuplicateScenarios(featureDirectory);
		}

		@AfterSuite
		public void navigateToReportFolder() throws IOException {
			String OS = System.getProperty("os.name");
			if(!OS.contains("Mac OS")){
				Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "\\" + ExtentManager.baseReportFolder + "ExtentReport.html"));
			}else {
				Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "/" + ExtentManager.baseReportFolder + "ExtentReport.html"));
			}
		}
	}
}
