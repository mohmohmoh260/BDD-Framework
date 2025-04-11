package builds.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * Provides browser driver setup for various supported browsers including Chrome, Firefox, Edge, Safari, etc.
 * Handles global vs per-test browser selection logic based on configuration provided in testNG.xml.
 * Extends {@link MainDriver} to inherit thread-safe WebDriver management.
 */
public class BrowserDriver extends MainDriver {

    /**
     * Sets up the browser driver for local execution using WebDriverManager.
     * Selects the browser type based on the global or per-test configuration from testNG.xml.
     *
     * @param browserType The browser type specified at the <test> level in testNG.xml (e.g., "chrome", "firefox").
     * @param URL         The key name of the URL to open, retrieved from globalDeviceParameter.
     */
    public void setupBrowserDriver(String browserType, String URL){
        // Global Setup: if global override is enabled
        if(globalDeviceParameter.get().get(0).get("globalBrowserTypeState").equalsIgnoreCase("false")){
            if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("chrome")){
                driver.set(WebDriverManager.chromedriver().create());
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("firefox")){
                driver.set(WebDriverManager.firefoxdriver().create());
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("edge")){
                driver.set(WebDriverManager.edgedriver().create());
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("chromium")){
                driver.set(WebDriverManager.chromiumdriver().create());
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("ie")){
                driver.set(WebDriverManager.iedriver().create());
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("safari")){
                driver.set(WebDriverManager.safaridriver().create());
            } else {
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <suite> in testNG.xml is incorrect");
                driver.set(WebDriverManager.chromiumdriver().create());
            }
            // Test-level Setup: if global override is disabled
        } else {
            if(browserType.equalsIgnoreCase("chrome")){
                driver.set(WebDriverManager.chromiumdriver().create());
            } else if(browserType.equalsIgnoreCase("firefox")){
                driver.set(WebDriverManager.firefoxdriver().create());
            } else if(browserType.equalsIgnoreCase("edge")){
                driver.set(WebDriverManager.edgedriver().create());
            } else if(browserType.equalsIgnoreCase("chromium")){
                driver.set(WebDriverManager.chromiumdriver().create());
            } else if(browserType.equalsIgnoreCase("ie")){
                driver.set(WebDriverManager.iedriver().create());
            } else if(browserType.equalsIgnoreCase("safari")){
                driver.set(WebDriverManager.safaridriver().create());
            } else {
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <test> in testNG.xml is incorrect");
                driver.set(WebDriverManager.chromiumdriver().create());
            }
        }

        // Apply implicit wait time and navigate to the target URL
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Long.parseLong(globalDeviceParameter.get().get(0).get("implicitwait"))));
        driver.get().get(globalDeviceParameter.get().get(0).get(URL));
    }
}