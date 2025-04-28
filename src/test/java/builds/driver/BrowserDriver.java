package builds.driver;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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
                System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
                ChromeOptions options = new ChromeOptions();
                options.setAcceptInsecureCerts(true);
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("autofill.profile_enabled", false);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                options.setUnhandledPromptBehaviour(org.openqa.selenium.UnexpectedAlertBehaviour.IGNORE);
                driver.set(new ChromeDriver(options));
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("firefox")){
                System.setProperty("webdriver.gecko.driver", "driver/geckodriver");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setAcceptInsecureCerts(true);
                firefoxOptions.addArguments(
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
                driver.set(new FirefoxDriver(firefoxOptions));
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("edge")){
                System.setProperty("webdriver.edge.driver", "driver/msedgedriver");
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setAcceptInsecureCerts(true);
                Map<String, Object> edgePrefs = new HashMap<>();
                edgePrefs.put("autofill.profile_enabled", false);
                edgeOptions.setExperimentalOption("prefs", edgePrefs);
                edgeOptions.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                edgeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
                driver.set(new EdgeDriver(edgeOptions));
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("chromium")){
                System.setProperty("webdriver.chrome.driver", "driver/chromedriver"); // Chromedriver works for Chromium too!
                ChromeOptions chromiumOptions = new ChromeOptions();
                chromiumOptions.setAcceptInsecureCerts(true);
                Map<String, Object> chromiumPrefs = new HashMap<>();
                chromiumPrefs.put("autofill.profile_enabled", false);
                chromiumOptions.setExperimentalOption("prefs", chromiumPrefs);
                chromiumOptions.setBinary("/path/to/your/chromium"); // **IMPORTANT: Set Chromium binary path manually**
                chromiumOptions.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                chromiumOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
                driver.set(new ChromeDriver(chromiumOptions));
            } else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("safari")){
                driver.set(new SafariDriver());
            } else {
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <suite> in testNG.xml is incorrect");
                System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
                ChromeOptions options = new ChromeOptions();
                options.setAcceptInsecureCerts(true);
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("autofill.profile_enabled", false);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                options.setUnhandledPromptBehaviour(org.openqa.selenium.UnexpectedAlertBehaviour.IGNORE);
                driver.set(new ChromeDriver(options));
            }
            // Test-level Setup: if global override is disabled
        } else {
            if(browserType.equalsIgnoreCase("chrome")){
                System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
                ChromeOptions options = new ChromeOptions();
                options.setAcceptInsecureCerts(true);
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("autofill.profile_enabled", false);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                options.setUnhandledPromptBehaviour(org.openqa.selenium.UnexpectedAlertBehaviour.IGNORE);
                driver.set(new ChromeDriver(options));
            } else if(browserType.equalsIgnoreCase("firefox")){
                System.setProperty("webdriver.gecko.driver", "driver/geckodriver");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setAcceptInsecureCerts(true);
                firefoxOptions.addArguments(
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
                driver.set(new FirefoxDriver(firefoxOptions));
            } else if(browserType.equalsIgnoreCase("edge")){
                System.setProperty("webdriver.edge.driver", "driver/msedgedriver");
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setAcceptInsecureCerts(true);
                Map<String, Object> edgePrefs = new HashMap<>();
                edgePrefs.put("autofill.profile_enabled", false);
                edgeOptions.setExperimentalOption("prefs", edgePrefs);
                edgeOptions.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                edgeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
                driver.set(new EdgeDriver(edgeOptions));
            } else if(browserType.equalsIgnoreCase("chromium")){
                System.setProperty("webdriver.chrome.driver", "driver/chromedriver"); // Chromedriver works for Chromium too!
                ChromeOptions chromiumOptions = new ChromeOptions();
                chromiumOptions.setAcceptInsecureCerts(true);
                Map<String, Object> chromiumPrefs = new HashMap<>();
                chromiumPrefs.put("autofill.profile_enabled", false);
                chromiumOptions.setExperimentalOption("prefs", chromiumPrefs);
                chromiumOptions.setBinary("/path/to/your/chromium"); // **IMPORTANT: Set Chromium binary path manually**
                chromiumOptions.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                chromiumOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
                driver.set(new ChromeDriver(chromiumOptions));
            }else if(browserType.equalsIgnoreCase("safari")){
                driver.set(new SafariDriver());
            } else {
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <test> in testNG.xml is incorrect");
                System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
                ChromeOptions options = new ChromeOptions();
                options.setAcceptInsecureCerts(true);
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("autofill.profile_enabled", false);
                options.setExperimentalOption("prefs", prefs);
                options.addArguments(
                        "--disable-web-security",
                        "--ignore-certificate-errors",
                        "--allow-running-insecure-content"
                );
                options.setUnhandledPromptBehaviour(org.openqa.selenium.UnexpectedAlertBehaviour.IGNORE);
                driver.set(new ChromeDriver(options));
            }
        }

        // Apply implicit wait time and navigate to the target URL
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Long.parseLong(globalDeviceParameter.get().get(0).get("implicitwait"))));
        driver.get().get(globalDeviceParameter.get().get(0).get(URL));
    }
}