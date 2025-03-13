package builds.driver;

import org.json.JSONObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;

public class BrowserStackInstance extends DriverType{

    private JSONObject jsonConfig = new JSONObject();

    public RemoteWebDriver setupRemoteDriver() throws IOException {
        if (remoteDriver.get() == null) {
            remoteDriver.set(new RemoteWebDriver(new URL(getBrowserStackURL()), getCapabilities()));
        }
        isRemoteDriver.set(true);
        return remoteDriver.get();
    }

    private String getBrowserStackURL() {
        return "https://" + System.getenv("BROWSERSTACK_USERNAME") + ":" +
                System.getenv("BROWSERSTACK_ACCESS_KEY") +
                "@hub-cloud.browserstack.com/wd/hub";
    }

    private DesiredCapabilities getCapabilities() throws IOException {
        File file = new File("src/test/resources/browserstack.conf.json");
        String content = new String(Files.readAllBytes(file.toPath()));
        jsonConfig = new JSONObject(content);

        DesiredCapabilities caps = new DesiredCapabilities();

        if (getRemoteDriverPlatform().equals("web")) {
            caps.setBrowserName(System.getProperty("browser", jsonConfig.getString("browser")));
            caps.setVersion(jsonConfig.getString("browserVersion"));
            caps.setPlatform(Platform.fromString(jsonConfig.getString("os")));
            isWebDriver.set(true);
        } else {
            HashMap<String, Object> browserstackOptions = new HashMap<>();
            browserstackOptions.put("deviceName", jsonConfig.getString("deviceName"));
            browserstackOptions.put("osVersion", jsonConfig.getString("osVersion"));
            browserstackOptions.put("app", jsonConfig.getString("app"));
            browserstackOptions.put("realMobile", jsonConfig.getBoolean("realMobile"));

            if (getRemoteDriverPlatform().equals("android")) {
                caps.setCapability("platformName", "Android");
                isAndroid.set(true);
            } else if (getRemoteDriverPlatform().equals("ios")) {
                caps.setCapability("platformName", "iOS");
                isIOS.set(true);
            }

            isAppiumDriver.set(true);
            caps.setCapability("bstack:options", browserstackOptions);
        }

        return caps;
    }

    private String getRemoteDriverPlatform(){
        return System.getProperty("platform", jsonConfig.getString("platform")).toLowerCase();
    }

    public void quitDriver() {
        if (remoteDriver.get() != null) {
            remoteDriver.get().quit();
            remoteDriver.remove();
        }
    }
}