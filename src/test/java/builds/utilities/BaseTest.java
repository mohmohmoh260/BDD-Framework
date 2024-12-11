package builds.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected static AppiumDriver mobileDriver;



    public static void androidSetup() {
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        boolean fullReset = true;
        boolean noReset = true;
        if(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.fullreset").equals("false")){
            fullReset = false;
        }
        if(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.noreset").equals("false")){
            noReset = false;
        }
        uiAutomator2Options.setUdid(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.udid"))
                        .setPlatformName(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.platformName"))
                                .setAutomationName(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.automationName"))
                                        .setAppPackage(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.app.package"))
                                                 .setAppActivity(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.app.activity"))
                                                         .setFullReset(fullReset)
                                                                 .setNoReset(noReset)
                                                                        .setNewCommandTimeout(Duration.ofSeconds(300))
                                                                                .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(300));

        uiAutomator2Options.setCapability("unicodeKeyboard", true);
        uiAutomator2Options.setCapability("resetKeyboard", true);

        String OS = System.getProperty("os.name");
        if(!OS.contains("Mac OS")){
            String temp = GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.apk");
            uiAutomator2Options.setApp(System.getProperty("user.dir")+"\\"+temp.replace("/", "\\"));
        }else {
            String temp = GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.apk");
            uiAutomator2Options.setApp("/Users/00131018amirulhakim/Documents/com.test 2.framework/Apk/NotePad.apk");
        }

        try {
            mobileDriver = new AndroidDriver(new URL(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.server.url")), uiAutomator2Options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // Initiate Page Factory For all Page Class
        BasePages.setMobilePageFactory(mobileDriver);

        GlobalProperties.setGlobalVariableProperties("currentDriverPlatform", "android");
    }

    public static void iOSSetup(){
        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
        boolean fullReset = true;
        boolean noReset = true;
        if(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.fullreset").equals("false")){
            fullReset = false;
        }
        if(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.noreset").equals("false")){
            noReset = false;
        }
        xcuiTestOptions.setUdid(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.udid"))
                .setPlatformName(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.platformName"))
                        .setPlatformVersion(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.platformVersion"))
                                .setAutomationName(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.automationName"))
                                        .setBundleId(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.ios.bundleId"))
                                                .setFullReset(fullReset)
                                                        .setNoReset(noReset);

        xcuiTestOptions.setCapability("unicodeKeyboard", true);
        xcuiTestOptions.setCapability("resetKeyboard", true);
        try {
            mobileDriver = new IOSDriver(new URL(GlobalProperties.getGlobalVariablesProperties().getProperty("mobile.server.url")), xcuiTestOptions);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // Initiate Page Factory For all Page Class
        BasePages.setMobilePageFactory(mobileDriver);

        GlobalProperties.setGlobalVariableProperties("currentDriverPlatform", "iOS");
    }



    public static AppiumDriver getMobileDriver(){
        return mobileDriver;
    }

}
