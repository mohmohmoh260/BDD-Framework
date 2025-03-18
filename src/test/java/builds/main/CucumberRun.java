package builds.main;

import io.cucumber.testng.*;
import org.testng.annotations.AfterSuite;
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
