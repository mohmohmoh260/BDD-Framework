package builds.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class MobileInstance{
    private static final ThreadLocal<AppiumDriver> mobileDriver = new ThreadLocal<>();
    private AppiumDriverLocalService appiumDriverLocalService;

    protected void mobileInit(String testName) {
        TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
        List<Map<String, String>> deviceParameters = testNGXmlParser.filterXMLByTestName(testName);
        List<Map<String,String>> globalDeviceParameter = testNGXmlParser.getGlobalParameters();

        String platformName = deviceParameters.get(0).get("platformName");
        String udid = deviceParameters.get(0).get("udid");
        String automationName = deviceParameters.get(0).get("automationName");
        String deviceName = deviceParameters.get(0).get("deviceName");
        String platformVersion = deviceParameters.get(0).get("platformVersion");
        String bundleID = deviceParameters.get(0).get("bundleID");
        String appPackage = deviceParameters.get(0).get("appPackage");
        String appActivity = deviceParameters.get(0).get("appActivity");
        String fullReset = deviceParameters.get(0).get("fullReset");
        String noReset = deviceParameters.get(0).get("noReset");
        String useNewWDA = deviceParameters.get(0).get("useNewWDA");
        String autoGrantPermissions = globalDeviceParameter.get(0).get("autoGrantPermissions");
        String autoAcceptAlerts = globalDeviceParameter.get(0).get("autoAcceptAlerts");
        String autoDismissAlerts = globalDeviceParameter.get(0).get("autoDismissAlerts");
        String appiumServerIP = globalDeviceParameter.get(0).get("appiumServerIP");

        int min = 5000;  // Minimum port number
        int max = 65535; // Maximum port number
        String port = String.valueOf((int) (Math.random() * (max - min + 1)) + min);
        startAppiumService(appiumServerIP, port);

        if (platformName.equalsIgnoreCase("android")) {
            UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
            uiAutomator2Options
                    .setUdid(udid)
                    .setPlatformName(platformName)
                    .setAutomationName(automationName)
                    .setDeviceName(deviceName)
                    .setPlatformVersion(platformVersion)
                    .setAppPackage(appPackage)
                    .setAppActivity(appActivity)
                    .setFullReset(Boolean.parseBoolean(fullReset))
                    .setNoReset(Boolean.parseBoolean(noReset))
                    .setNewCommandTimeout(Duration.ofSeconds(600))
                    .setAutoGrantPermissions(Boolean.parseBoolean(autoGrantPermissions));

            uiAutomator2Options.setCapability("autoAcceptAlerts", Boolean.parseBoolean(autoAcceptAlerts));
            uiAutomator2Options.setCapability("autoDismissAlerts", Boolean.parseBoolean(autoDismissAlerts));

            String apkPath = deviceParameters.get(0).get("apkPath");
            if(!apkPath.isEmpty()){
                String OS = System.getProperty("os.name");
                if (!OS.contains("Mac OS")) {
                    uiAutomator2Options.setApp(System.getProperty("user.dir") + "\\" + apkPath.replace("/", "\\"));
                } else {
                    uiAutomator2Options.setApp(System.getProperty("user.dir") + "/" + apkPath.replace("\\", "/"));
                }
            }

            try {
                setMobileDriver(new AndroidDriver(new URL("http://"+appiumServerIP + ":" + port), uiAutomator2Options));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } else if (platformName.equalsIgnoreCase("iOS")) {
            XCUITestOptions xcuiTestOptions = new XCUITestOptions();

            xcuiTestOptions
                    .setUdid(udid)
                    .setPlatformName(platformName)
                    .setAutomationName(automationName)
                    .setDeviceName(deviceName)
                    .setPlatformVersion(platformVersion)
                    .setBundleId(bundleID)
                    .setFullReset(Boolean.parseBoolean(fullReset))
                    .setNoReset(Boolean.parseBoolean(noReset))
                    .setAutoDismissAlerts(Boolean.parseBoolean(autoDismissAlerts))
                    .setAutoAcceptAlerts(Boolean.parseBoolean(autoAcceptAlerts))
                    .setUseNewWDA(Boolean.parseBoolean(useNewWDA));

            xcuiTestOptions.setCapability("autoGrantPermissions", Boolean.parseBoolean(autoGrantPermissions));

            String apkPath = deviceParameters.get(0).get("apkPath");
            if(!apkPath.isEmpty()){
                String OS = System.getProperty("os.name");
                if (!OS.contains("Mac OS")) {
                    xcuiTestOptions.setApp(System.getProperty("user.dir") + "\\" + apkPath.replace("/", "\\"));
                } else {
                    xcuiTestOptions.setApp(System.getProperty("user.dir") + "/" + apkPath.replace("\\", "/"));
                }
            }

            xcuiTestOptions.setCapability("clearKeychains", true);

            try {
                setMobileDriver(new IOSDriver(new URL("http://"+appiumServerIP + ":" + port), xcuiTestOptions));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.err.println("Check the platformName value in testng.xml. The value should be either \"android\" or \"iOS\"");
        }
        PageFactoryInit pageFactoryInit = new PageFactoryInit();
        pageFactoryInit.setMobilePageFactory(getMobileDriver());
    }

    private void startAppiumService(String appiumIP, String port){
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.withIPAddress(appiumIP);
        appiumServiceBuilder.usingPort(Integer.parseInt(port));
        appiumDriverLocalService = AppiumDriverLocalService.buildService(appiumServiceBuilder);
        appiumDriverLocalService.start();
    }

    public AppiumDriver getMobileDriver(){
        return mobileDriver.get();
    }

    private void setMobileDriver(AppiumDriver driver){
        mobileDriver.set(driver);
    }

    @AfterSuite @BeforeSuite
    private void quitMobileDriver() {
        if (getMobileDriver() != null) {
            getMobileDriver().quit();
            mobileDriver.remove();
        }
        if (appiumDriverLocalService != null && appiumDriverLocalService.isRunning()) {
            appiumDriverLocalService.stop();
        }
    }
}