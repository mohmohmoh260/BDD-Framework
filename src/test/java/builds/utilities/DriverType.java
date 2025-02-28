package builds.utilities;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

public class DriverType {

    protected static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isWebDriver = new ThreadLocal<>();
    protected static final ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isAndroidDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isIOSDriver = new ThreadLocal<>();
}
