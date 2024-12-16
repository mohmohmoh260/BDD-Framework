package builds.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

public class BrowserInstance {
    public static WebDriver driver;
    public static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    public BasePages basePages = new BasePages();

    public void browserInit(){
        if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("chrome")){
            driver = WebDriverManager.chromiumdriver().create();
        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("firefox")){
            driver = WebDriverManager.firefoxdriver().create();
        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("edge")){
            driver = WebDriverManager.edgedriver().create();
        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("chromium")){
            driver = WebDriverManager.chromiumdriver().create();
        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("ie")){
            driver = WebDriverManager.iedriver().create();
        }else if(GlobalProperties.getGlobalVariablesProperties().getProperty("browser.type").equalsIgnoreCase("safari")){
            driver = WebDriverManager.safaridriver().create();
        }else{
            System.out.println("Please check your browser.type values correctly in Global Variable properties file");
            System.exit(1);
        }
        setWebDriver(driver);
        // Initiate Page Factory For all Page Class
        basePages.setWebPageFactory(driver);
        GlobalProperties.setGlobalVariableProperties("currentDriverPlatform", "browser");
    }

    public WebDriver getWebDriver(){
        return webDriver.get();
    }

    private void setWebDriver(WebDriver driver){
       System.out.println(driver);
        webDriver.set(driver);
    }

    public void quitWebDriver(){
        if (getWebDriver() != null) {
            getWebDriver().quit();
            webDriver.remove();
        }
    }

}
