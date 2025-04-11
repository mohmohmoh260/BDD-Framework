package builds.driver;

import builds.elements.ElementInstance;
import org.openqa.selenium.WebDriver;

/**
 * Abstract base class that sets up a thread-safe WebDriver instance
 * for test execution. All driver-related classes (e.g., BrowserDriver, MobileDriver)
 * extend this class to access and manage WebDriver instances in parallel environments.
 *
 * <p>This class uses a {@link ThreadLocal} to ensure thread safety, allowing
 * multiple tests to run in parallel without driver conflicts.</p>
 *
 * <p>Extends {@link ElementInstance} to inherit access to element utilities.</p>
 */
public abstract class MainDriver extends ElementInstance {

    /**
     * Thread-local WebDriver instance to ensure each thread has its own WebDriver.
     * Prevents cross-test contamination when running in parallel.
     */
    protected static final ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);
}