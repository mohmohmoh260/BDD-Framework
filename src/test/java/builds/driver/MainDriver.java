package builds.driver;

import builds.elements.ElementInstance;
import org.openqa.selenium.WebDriver;

public abstract class MainDriver extends ElementInstance {
    protected static final ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);
}
