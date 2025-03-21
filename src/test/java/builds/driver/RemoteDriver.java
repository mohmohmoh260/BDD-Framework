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

public class RemoteDriver extends MainDriver {

    public void setupRemoteDriver(String platform, String URL) throws IOException {
        if (driver.get() == null) {
            driver.set(new RemoteWebDriver(new URL(getBrowserStackURL()), getCapabilities(platform)));
            driver.get().get(globalDeviceParameter.get().get(0).get(URL));
        }
    }

    private String getBrowserStackURL() {
        return "https://amirul_D6CMwe:FqzCyn83EZ5zx7xLy71C@hub-cloud.browserstack.com/wd/hub";
    }

    private static DesiredCapabilities getCapabilities(String platform) throws IOException {
        String configFilePath;

        // Determine which config file to load
        if (platform.equals("web")) {
            configFilePath = "src/test/resources/browserstack.web.config.json";
        } else {
            configFilePath = "src/test/resources/browserstack.mobile.config.json";
        }

        File file = new File(configFilePath);
        if (!file.exists()) {
            throw new RuntimeException("❌ Config file not found: " + configFilePath);
        }

        String content = new String(Files.readAllBytes(file.toPath()));
        JSONObject jsonConfig = new JSONObject(content);

        DesiredCapabilities caps = new DesiredCapabilities();

        if (platform.equals("web")) {
            JSONObject webConfig = jsonConfig.getJSONObject("web");
            caps.setBrowserName(System.getProperty("browser", webConfig.getString("browser")));
            caps.setVersion(webConfig.getString("browserVersion"));
            caps.setPlatform(Platform.fromString(webConfig.getString("os")));
        } else {
            JSONObject mobileConfig = jsonConfig.getJSONObject(platform); // "android" or "ios"

            HashMap<String, Object> browserstackOptions = new HashMap<>();
            browserstackOptions.put("deviceName", mobileConfig.getString("deviceName"));
            browserstackOptions.put("osVersion", mobileConfig.getString("osVersion"));
            browserstackOptions.put("app", mobileConfig.getString("app"));
            browserstackOptions.put("realMobile", mobileConfig.getBoolean("realMobile"));

            caps.setCapability("platformName", platform.equals("android") ? "Android" : "iOS");
            caps.setCapability("bstack:options", browserstackOptions);
        }

        return caps;
    }
}