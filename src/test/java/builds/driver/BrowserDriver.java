package builds.driver;

import builds.extent.ExtentManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.logging.Logger;

public class BrowserDriver extends MainDriver {

    public void setupBrowserDriver(String browserType, String URL){
        // Global Setup
        if(globalDeviceParameter.get().get(0).get("globalBrowserTypeState").equalsIgnoreCase("false")){
            if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("chrome")){
                driver.set(WebDriverManager.chromedriver().create());
            }else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("firefox")){
                driver.set(WebDriverManager.firefoxdriver().create());
            }else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("edge")){
                driver.set(WebDriverManager.edgedriver().create());
            }else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("chromium")){
                driver.set(WebDriverManager.chromiumdriver().create());
            }else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("ie")){
                driver.set(WebDriverManager.iedriver().create());
            }else if(globalDeviceParameter.get().get(0).get("globalBrowserType").equalsIgnoreCase("safari")){
                driver.set(WebDriverManager.safaridriver().create());
            }else{
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <suite> in testNG.xml is incorrect");
                driver.set(WebDriverManager.chromiumdriver().create());
            }
        // Test Suite Level Setup
        }else{
            if(browserType.equalsIgnoreCase("chrome")){
                driver.set(WebDriverManager.chromiumdriver().create());
            }else if(browserType.equalsIgnoreCase("firefox")){
                driver.set(WebDriverManager.firefoxdriver().create());
            }else if(browserType.equalsIgnoreCase("edge")){
                driver.set(WebDriverManager.edgedriver().create());
            }else if(browserType.equalsIgnoreCase("chromium")){
                driver.set(WebDriverManager.chromiumdriver().create());
            }else if(browserType.equalsIgnoreCase("ie")){
                driver.set(WebDriverManager.iedriver().create());
            }else if(browserType.equalsIgnoreCase("safari")){
                driver.set(WebDriverManager.safaridriver().create());
            }else{
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <test> in testNG.xml is incorrect");
                driver.set(WebDriverManager.chromiumdriver().create());
            }
        }
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(globalDeviceParameter.get().get(0).get("implicitwait"))));
        driver.get().get(globalDeviceParameter.get().get(0).get(URL));
    }
}
