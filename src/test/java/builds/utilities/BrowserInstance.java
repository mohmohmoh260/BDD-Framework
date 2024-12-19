package builds.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;

public class BrowserInstance extends PageFactoryInit{
    private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    protected void browserInit(String browserType){
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
            System.out.println("Please check your browser.type values correctly in Global Variable properties file");
            System.exit(1);
        }
        setWebPageFactory(getWebDriver());
    }

    public WebDriver getWebDriver(){
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
}
