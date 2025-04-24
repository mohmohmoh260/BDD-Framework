package builds.listener;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

/**
 * StepListener listens to Cucumber test step events during execution.
 * <p>
 * It captures the Gherkin step text at runtime and stores it using a ThreadLocal
 * so it can be accessed during step execution, useful for logging, debugging, or custom reporting.
 */
public class StepListener implements ConcurrentEventListener {

    public static ThreadLocal<Throwable> lastStepError = new ThreadLocal<>();

    /**
     * Thread-local variable to store the current Gherkin step being executed.
     * <p>
     * Useful in multi-threaded test execution scenarios where step text
     * needs to be uniquely retained per thread.
     */
    public static final ThreadLocal<String> gherkinStep = new ThreadLocal<>();

    /**
     * Registers event handler for step start events.
     *
     * @param publisher The event publisher used to hook into Cucumber's event system.
     */
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this::handleStepStarted);
    }

    /**
     * Captures the Gherkin step text when a test step starts.
     *
     * @param event The Cucumber TestStepStarted event.
     */
    private void handleStepStarted(TestStepStarted event) {
        TestStep testStep = event.getTestStep();
        if (testStep instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) testStep;
            gherkinStep.set(pickleStep.getStep().getText());
        }
    }
}