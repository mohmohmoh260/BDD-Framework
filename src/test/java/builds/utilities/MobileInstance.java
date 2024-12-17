package builds.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class MobileInstance {
    private static String device;
    private static AppiumDriver driver;
    private static final ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();
    private PageFactoryInit pageFactoryInit = new PageFactoryInit();

    public void mobileInit(String platformName, String appiumServerIP, String port, String udid, String automationName, String appPackage, String appActivity, String fullReset, String noReset, String apkPath, String bundleID, String deviceName, String platformVersion) {
        if (platformName.equalsIgnoreCase("android")) {
            startAppiumService(appiumServerIP, port);
            UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
            uiAutomator2Options.setUdid(udid)
                    .setPlatformName(platformName)
                    .setAutomationName(automationName)
                    .setAppPackage(appPackage)
                    .setAppActivity(appActivity)
                    .setFullReset(Boolean.parseBoolean(fullReset))
                    .setNoReset(Boolean.parseBoolean(noReset))
                    .setNewCommandTimeout(Duration.ofSeconds(300))
                    .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(300));

            uiAutomator2Options.setCapability("unicodeKeyboard", true);
            uiAutomator2Options.setCapability("resetKeyboard", true);

            String OS = System.getProperty("os.name");
            if (!OS.contains("Mac OS")) {
                uiAutomator2Options.setApp(System.getProperty("user.dir") + "\\" + apkPath.replace("/", "\\"));
            } else {
                uiAutomator2Options.setApp(System.getProperty("user.dir") + "/" + apkPath.replace("\\", "/"));
            }

            try {
                setMobileDriver(new AndroidDriver(new URL("http://"+appiumServerIP + ":" + port), uiAutomator2Options));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } else if (platformName.equalsIgnoreCase("iOS")) {
            startAppiumService(appiumServerIP, port);
            XCUITestOptions xcuiTestOptions = new XCUITestOptions();
            xcuiTestOptions.setUdid(udid)
                    .setPlatformName(platformName)
                    .setPlatformVersion(platformVersion)
                    .setDeviceName(deviceName)
                    .setAutomationName(automationName)
                    .setBundleId(bundleID)
                    .setFullReset(Boolean.parseBoolean(fullReset))
                    .setNoReset(Boolean.parseBoolean(noReset));

            xcuiTestOptions.setCapability("unicodeKeyboard", true);
            xcuiTestOptions.setCapability("resetKeyboard", true);
            try {
                setMobileDriver(new IOSDriver(new URL("http://"+appiumServerIP + ":" + port), xcuiTestOptions));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.err.println("Check the platformName value in testng.xml. The value should be either \"android\" or \"iOS\"");
        }
        pageFactoryInit.setMobilePageFactory(driver);
    }

    public void startAppiumService(String appiumIP, String port){
        AppiumDriverLocalService appiumDriverLocalService;
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.withIPAddress(appiumIP);
        appiumServiceBuilder.usingPort(Integer.parseInt(port));
        appiumDriverLocalService = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        appiumDriverLocalService.start();
    }

    public static AppiumDriver getMobileDriver(){
        return mobileDriver.get();
    }

    private void setMobileDriver(AppiumDriver driver){
        mobileDriver.set(driver);
    }

    public void quitMobileDriver(){
        if (getMobileDriver() != null) {
            getMobileDriver().quit();
            mobileDriver.remove();
        }
    }
}
