package builds.driver;

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

/**
 * Handles setup and initialization of local Appium mobile drivers for Android and iOS platforms.
 * Extends {@link MainDriver} to use shared thread-local driver management.
 */
public class MobileDriver extends MainDriver {

    /**
     * Thread-local storage for the Appium server instance.
     * Ensures each test thread runs with its own Appium service.
     */
    private static final ThreadLocal<AppiumDriverLocalService> appiumDriverLocalService = new ThreadLocal<>();

    /**
     * Initializes the mobile driver based on platform-specific parameters provided via XML configuration.
     *
     * @param testName The name of the test to retrieve corresponding device and app capabilities.
     */
    public void setupMobileDriver(String testName) {
        List<Map<String, String>> deviceParameters = filterXMLByTestName(testName);

        String platformName = deviceParameters.get(0).get("platformName");
        String udid = deviceParameters.get(0).get("udid");
        String deviceName = deviceParameters.get(0).get("deviceName");
        String platformVersion = deviceParameters.get(0).get("platformVersion");
        String bundleID = deviceParameters.get(0).get("bundleID");
        String appPackage = deviceParameters.get(0).get("appPackage");
        String appActivity = deviceParameters.get(0).get("appActivity");
        String browserType = deviceParameters.get(0).get("browserType");
        String fullReset = deviceParameters.get(0).get("fullReset");
        String noReset = deviceParameters.get(0).get("noReset");

        int min = 5000;
        int max = 65535;
        String port = String.valueOf((int) (Math.random() * (max - min + 1)) + min);
        startAppiumService(port);

        if (platformName.equalsIgnoreCase("android")) {
            UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
            uiAutomator2Options
                    .setUdid(udid)
                    .setPlatformName(platformName)
                    .setAutomationName("UiAutomator2")
                    .setDeviceName(deviceName)
                    .setPlatformVersion(platformVersion)
                    .setAppPackage(appPackage)
                    .setAppActivity(appActivity)
                    .setFullReset(Boolean.parseBoolean(fullReset))
                    .setNoReset(Boolean.parseBoolean(noReset))
                    .setNewCommandTimeout(Duration.ofSeconds(600))
                    .setAutoGrantPermissions(true);

            uiAutomator2Options.setCapability("autoAcceptAlerts", true);
            uiAutomator2Options.setCapability("autoDismissAlerts", true);

            String apkPath = deviceParameters.get(0).get("apkPath");
            if (!apkPath.isEmpty()) {
                String OS = System.getProperty("os.name");
                if (!OS.contains("Mac OS")) {
                    uiAutomator2Options.setApp(System.getProperty("user.dir") + "\\" + apkPath.replace("/", "\\"));
                } else {
                    uiAutomator2Options.setApp(System.getProperty("user.dir") + "/" + apkPath.replace("\\", "/"));
                }
            }

            try {
                driver.set(new AndroidDriver(new URL("http://127.0.0.1:" + port), uiAutomator2Options));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } else if (platformName.equalsIgnoreCase("iOS")) {
            XCUITestOptions xcuiTestOptions = new XCUITestOptions();
            xcuiTestOptions
                    .setUdid(udid)
                    .setPlatformName(platformName)
                    .setAutomationName("XCUITest")
                    .setDeviceName(deviceName)
                    .setPlatformVersion(platformVersion)
                    .setBundleId(bundleID)
                    .setFullReset(Boolean.parseBoolean(fullReset))
                    .setNoReset(Boolean.parseBoolean(noReset))
                    .setAutoDismissAlerts(true)
                    .setAutoAcceptAlerts(true)
                    .setUseNewWDA(true)
                    .setCapability("browserType", browserType);

            xcuiTestOptions.setCapability("autoGrantPermissions", true);

            String apkPath = deviceParameters.get(0).get("apkPath");
            if (!apkPath.isEmpty()) {
                String OS = System.getProperty("os.name");
                if (!OS.contains("Mac OS")) {
                    xcuiTestOptions.setApp(System.getProperty("user.dir") + "\\" + apkPath.replace("/", "\\"));
                } else {
                    xcuiTestOptions.setApp(System.getProperty("user.dir") + "/" + apkPath.replace("\\", "/"));
                }
            }

            xcuiTestOptions.setCapability("clearKeychains", true);

            try {
                driver.set(new IOSDriver(new URL("http://127.0.0.1:" + port), xcuiTestOptions));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.err.println("Check the platformName value in testng.xml. The value should be either \"android\" or \"iOS\"");
        }

        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(120));
    }

    /**
     * Starts a new local Appium service using a randomly generated port.
     *
     * @param port The port number for the Appium server to bind to.
     */
    private void startAppiumService(String port){
        AppiumServiceBuilder appiumServiceBuilder = new AppiumServiceBuilder();
        appiumServiceBuilder.withIPAddress("127.0.0.1");
        appiumServiceBuilder.usingPort(Integer.parseInt(port));
        appiumDriverLocalService.set(AppiumDriverLocalService.buildService(appiumServiceBuilder));
        appiumDriverLocalService.get().start();
    }
}