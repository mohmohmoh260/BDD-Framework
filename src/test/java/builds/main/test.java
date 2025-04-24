package builds.main;

import builds.extent.ExtentManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class test {

    public static void main(String[] args) {
        ExtentTest parentTest = ExtentManager.getInstance().createTest("Parent Test");
        ExtentManager.setExtent(parentTest);

        ExtentManager.createAndPushNode("Child 1");
        ExtentManager.bufferLog(Status.INFO, "Log A", null);
        ExtentManager.popNode();

        ExtentManager.createAndPushNode("Child 2");
        ExtentManager.bufferLog(Status.INFO, "Log B", null);

        ExtentManager.createAndPushNode("Child 3");
        ExtentManager.bufferLog(Status.INFO, "Log X", null);
         // flush before pop
        ExtentManager.popNode();

        ExtentManager.bufferLog(Status.INFO, "Log Babi", null);
        ExtentManager.popNode();

        ExtentManager.createAndPushNode("Child 1");
        ExtentManager.bufferLog(Status.INFO, "Log C", null);
        ExtentManager.popNode();

        ExtentManager.flush();
    }
}
