package builds.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

public class BrowserInstance {
    protected static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    public static void browserSetup(){
        if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("chrome")){
            webDriver.set(WebDriverManager.chromiumdriver().create());

        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("firefox")){
            webDriver.set(WebDriverManager.firefoxdriver().create());

        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("edge")){
            webDriver.set(WebDriverManager.edgedriver().create());

        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("chromium")){
            webDriver.set(WebDriverManager.chromiumdriver().create());

        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("ie")){
            webDriver.set(WebDriverManager.iedriver().create());

        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("safari")){
            webDriver.set(WebDriverManager.safaridriver().create());

        }else{
            System.out.println("Please check your browser.type values correctly in Global Variable properties file");
            System.exit(1);
        }
        // Initiate Page Factory For all Page Class
        BasePages.setWebPageFactory((WebDriver) webDriver);

        GlobalProperties.setGlobalVariableProperties("currentDriverPlatform", "browser");
    }

//    public static WebDriver getWebDriver(){
//
//    }
}
