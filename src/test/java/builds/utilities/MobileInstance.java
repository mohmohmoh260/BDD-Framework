package builds.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class MobileInstance extends DriverType{

    private static final ThreadLocal<AppiumDriverLocalService> appiumDriverLocalService = new ThreadLocal<>();

    protected void mobileInit(String testName) {
        List<Map<String, String>> deviceParameters = testNGXmlParser.filterXMLByTestName(testName);

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
                appiumDriver.set(new AndroidDriver(new URL("http://"+appiumServerIP + ":" + port), uiAutomator2Options));
                isWebDriver.set(false);
                isAppiumDriver.set(true);
                isAndroid.set(true);
                isIOS.set(false);
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
                appiumDriver.set(new IOSDriver(new URL("http://"+appiumServerIP + ":" + port), xcuiTestOptions));
                isWebDriver.set(false);
                isAppiumDriver.set(true);
                isAndroid.set(false);
                isIOS.set(true);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.err.println("Check the platformName value in testng.xml. The value should be either \"android\" or \"iOS\"");
        }
    }

    private void startAppiumService(String appiumIP, String port){
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.withIPAddress(appiumIP);
        appiumServiceBuilder.usingPort(Integer.parseInt(port));
        appiumDriverLocalService.set(AppiumDriverLocalService.buildService(appiumServiceBuilder));
        appiumDriverLocalService.get().start();
    }

    public void quitMobileDriver() {
        if (appiumDriver.get() != null) {
            appiumDriver.get().quit();
            appiumDriver.remove();
        }
        if (appiumDriverLocalService.get() != null && appiumDriverLocalService.get().isRunning()) {
            appiumDriverLocalService.get().stop();
        }
    }

    public AppiumDriver getMobileDriver(){
        return appiumDriver.get();
    }
}