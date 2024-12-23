package builds.utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BrowserInstance{
    private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    protected void browserInit(String browserType){
        TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
        List<Map<String,String>> globalDeviceParameter = testNGXmlParser.getGlobalParameters();

        // Global Setup
        if(globalDeviceParameter.get(0).get("globalBrowserTypeState").equalsIgnoreCase("false")){
            if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("chrome")){
                setWebDriver(WebDriverManager.chromiumdriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("firefox")){
                setWebDriver(WebDriverManager.firefoxdriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("edge")){
                setWebDriver(WebDriverManager.edgedriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("chromium")){
                setWebDriver(WebDriverManager.chromiumdriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("ie")){
                setWebDriver(WebDriverManager.iedriver().create());
            }else if(globalDeviceParameter.get(0).get("globalBrowserType").equalsIgnoreCase("safari")){
                setWebDriver(WebDriverManager.safaridriver().create());
            }else{
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <suite> in testNG.xml is incorrect");
                setWebDriver(WebDriverManager.chromiumdriver().create());
            }
        // Test Setup
        }else{
            if(browserType.equalsIgnoreCase("chrome")){
                setWebDriver(WebDriverManager.chromiumdriver().create());
            }else if(browserType.equalsIgnoreCase("firefox")){
                setWebDriver(WebDriverManager.firefoxdriver().create());
            }else if(browserType.equalsIgnoreCase("edge")){
                setWebDriver(WebDriverManager.edgedriver().create());
            }else if(browserType.equalsIgnoreCase("chromium")){
                setWebDriver(WebDriverManager.chromiumdriver().create());
            }else if(browserType.equalsIgnoreCase("ie")){
                setWebDriver(WebDriverManager.iedriver().create());
            }else if(browserType.equalsIgnoreCase("safari")){
                setWebDriver(WebDriverManager.safaridriver().create());
            }else{
                Logger logger = (Logger) LoggerFactory.getILoggerFactory();
                logger.info("Selecting default browser (chrome) because browserType from <test> in testNG.xml is incorrect");
                setWebDriver(WebDriverManager.chromiumdriver().create());
            }
        }

        PageFactoryInit pageFactoryInit = new PageFactoryInit();
        pageFactoryInit.setWebPageFactory(getWebDriver());
        getWebDriver().get(testNGXmlParser.getGlobalParameters().get(0).get("url"));
    }

    public WebDriver getWebDriver(){
        return webDriver.get();
    }

    private void setWebDriver(WebDriver driver){
        webDriver.set(driver);
    }

    @AfterSuite @BeforeSuite
    public void quitWebDriver(){
        if (getWebDriver() != null) {
            getWebDriver().quit();
            webDriver.remove();
        }
    }


}
