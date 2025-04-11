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

    public void setupRemoteDriver(String parentKey, String URL) throws IOException {
        if (driver.get() == null) {
            driver.set(new RemoteWebDriver(new URL(getBrowserStackURL()), getCapabilities(parentKey)));
            driver.get().get(globalDeviceParameter.get().get(0).get(URL));
        }
    }

    private String getBrowserStackURL() {
        return "https://amirul_D6CMwe:FqzCyn83EZ5zx7xLy71C@hub-cloud.browserstack.com/wd/hub";
    }

    private static DesiredCapabilities getCapabilities(String parentKey) throws IOException {
        String configFilePath;
        boolean isWeb = isWebConfig(parentKey);

        // Choose appropriate config file based on key
        configFilePath = isWeb
                ? "src/test/resources/browserstack.web.config.json"
                : "src/test/resources/browserstack.mobile.config.json";

        File file = new File(configFilePath);
        if (!file.exists()) {
            throw new RuntimeException("❌ Config file not found: " + configFilePath);
        }

        String content = new String(Files.readAllBytes(file.toPath()));
        JSONObject jsonConfig = new JSONObject(content);

        if (!jsonConfig.has(parentKey)) {
            throw new RuntimeException("❌ Parent key not found in config: " + parentKey);
        }

        JSONObject config = jsonConfig.getJSONObject(parentKey);
        DesiredCapabilities caps = new DesiredCapabilities();

        if (isWeb) {
            caps.setBrowserName(config.getString("browser"));
            caps.setVersion(config.getString("browserVersion"));
            caps.setPlatform(Platform.fromString(config.getString("os")));

            if (config.has("osVersion")) {
                HashMap<String, Object> browserstackOptions = new HashMap<>();
                browserstackOptions.put("osVersion", config.getString("osVersion"));
                caps.setCapability("bstack:options", browserstackOptions);
            }

        } else {
            HashMap<String, Object> browserstackOptions = new HashMap<>();
            browserstackOptions.put("deviceName", config.getString("deviceName"));
            browserstackOptions.put("osVersion", config.getString("osVersion"));
            browserstackOptions.put("app", config.getString("app"));
            browserstackOptions.put("realMobile", config.getBoolean("realMobile"));

            String platform = config.getString("platform");
            caps.setCapability("platformName", platform.equalsIgnoreCase("android") ? "Android" : "iOS");
            caps.setCapability("bstack:options", browserstackOptions);
        }

        return caps;
    }

    private static boolean isWebConfig(String parentKey) {
        // Any key that is not scenario1, scenario2, etc., is assumed to be a web key.
        return !(parentKey.toLowerCase().contains("scenario") || parentKey.equalsIgnoreCase("android") || parentKey.equalsIgnoreCase("ios"));
    }
}