package builds.utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BrowserInstance extends DriverType{

    protected void browserInit(String browserType){
        TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
        List<Map<String,String>> globalDeviceParameter = testNGXmlParser.getGlobalParameters();

        // Global Setup
        if(globalDeviceParameter.get(0).get("globalBrowserTypeState").equalsIgnoreCase("false")){
            if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("chrome")){
                webDriver.set(WebDriverManager.chromiumdriver().create());
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
        isWebDriver.set(true);
        isAndroidDriver.set(false);
        isIOSDriver.set(false);
        webDriver.get().get(testNGXmlParser.getGlobalParameters().get(0).get("url"));
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
