package builds.driver;

import org.openqa.selenium.WebDriver;

public abstract class MainDriver {
    protected static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
}
