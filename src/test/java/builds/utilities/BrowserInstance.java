package builds.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

import java.util.List;
import java.util.Map;

public class BrowserInstance {
    public static List<Map<String, String>> globalParameters = null;
    private static WebDriver driver = null;
    private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    private PageFactoryInit pageFactoryInit = new PageFactoryInit();

    public void browserInit(String browserType){
        if(browserType.equalsIgnoreCase("chrome")){
            driver = WebDriverManager.chromiumdriver().create();
        }else if(browserType.equalsIgnoreCase("firefox")){
            driver = WebDriverManager.firefoxdriver().create();
        }else if(browserType.equalsIgnoreCase("edge")){
            driver = WebDriverManager.edgedriver().create();
        }else if(browserType.equalsIgnoreCase("chromium")){
            driver = WebDriverManager.chromiumdriver().create();
        }else if(browserType.equalsIgnoreCase("ie")){
            driver = WebDriverManager.iedriver().create();
        }else if(browserType.equalsIgnoreCase("safari")){
            driver = WebDriverManager.safaridriver().create();
        }else{
            System.out.println("Please check your browser.type values correctly in Global Variable properties file");
            System.exit(1);
        }
        setWebDriver(driver);
        pageFactoryInit.setWebPageFactory(driver);
    }

    public static WebDriver getWebDriver(){
        return webDriver.get();
    }

    private void setWebDriver(WebDriver driver){
        webDriver.set(driver);
    }

    public void quitWebDriver(){
        if (getWebDriver() != null) {
            getWebDriver().quit();
            webDriver.remove();
        }
    }

    @BeforeSuite
    private void setGlobalParameters(){
        TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
        globalParameters = testNGXmlParser.getGlobalParametersOnly();
    }

    public static List<Map<String, String>> getGlobalParameters(){
        System.out.println(globalParameters);
        return globalParameters;
    }


}
