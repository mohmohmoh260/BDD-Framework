package builds.driver;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.logging.Logger;

public class BrowserInstance extends DriverType {

    protected void setupBrowserDriver(String browserType, String URL){
        // Global Setup
        if(globalDeviceParameter.get(0).get("globalBrowserTypeState").equalsIgnoreCase("false")){
            if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("chrome")){
                webDriver.set(WebDriverManager.chromedriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("firefox")){
                webDriver.set(WebDriverManager.firefoxdriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("edge")){
                webDriver.set(WebDriverManager.edgedriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("chromium")){
                webDriver.set(WebDriverManager.chromiumdriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("ie")){
                webDriver.set(WebDriverManager.iedriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("safari")){
                webDriver.set(WebDriverManager.safaridriver().create());
            }else{
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <suite> in testNG.xml is incorrect");
                webDriver.set(WebDriverManager.chromiumdriver().create());
            }
        // Test Suite Level Setup
        }else{
            if(browserType.equalsIgnoreCase("chrome")){
                webDriver.set(WebDriverManager.chromiumdriver().create());
            }else if(browserType.equalsIgnoreCase("firefox")){
                webDriver.set(WebDriverManager.firefoxdriver().create());
            }else if(browserType.equalsIgnoreCase("edge")){
                webDriver.set(WebDriverManager.edgedriver().create());
            }else if(browserType.equalsIgnoreCase("chromium")){
                webDriver.set(WebDriverManager.chromiumdriver().create());
            }else if(browserType.equalsIgnoreCase("ie")){
                webDriver.set(WebDriverManager.iedriver().create());
            }else if(browserType.equalsIgnoreCase("safari")){
                webDriver.set(WebDriverManager.safaridriver().create());
            }else{
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <test> in testNG.xml is incorrect");
                webDriver.set(WebDriverManager.chromiumdriver().create());
            }
        }
        webDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(globalDeviceParameter.get(0).get("implicitwait"))));
        webDriver.get().get(globalDeviceParameter.get(0).get(URL));
        isWebDriver.set(true);
        isAppiumDriver.set(false);
        isAndroid.set(false);
        isIOS.set(false);
    }

    public void quitWebDriver(){
        if (webDriver.get() != null) {
            webDriver.get().quit();
            webDriver.remove();
        }
    }

    public WebDriver getWebDriver(){
        return webDriver.get();
    }
}
