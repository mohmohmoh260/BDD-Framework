package builds.utilities;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DriverType extends ElementInstance{

    protected static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isWebDriver = new ThreadLocal<>();
    protected static final ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isAppiumDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isAndroid = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isIOS = new ThreadLocal<>();

    protected static final TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
    protected static final List<Map<String,String>> globalDeviceParameter = testNGXmlParser.getGlobalParameters();
}
