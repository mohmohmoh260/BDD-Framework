package builds.utilities;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

public class DriverType extends ElementInstance{

    protected static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isWebDriver = ThreadLocal.withInitial(() -> false);
    protected static final ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();
    protected static final ThreadLocal<Boolean> isAppiumDriver = ThreadLocal.withInitial(() -> false);
    protected static final ThreadLocal<Boolean> isAndroid = ThreadLocal.withInitial(() -> false);
    protected static final ThreadLocal<Boolean> isIOS = ThreadLocal.withInitial(() -> false);

    protected static final TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
    protected static final List<Map<String,String>> globalDeviceParameter = testNGXmlParser.getGlobalParameters();
}
