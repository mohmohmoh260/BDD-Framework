package builds.utilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class PageFactoryInit {
    private Set<Class> findAllClassesUsingReflectionsLibrary() {
        Reflections reflections = new Reflections("workDirectory.pageObject", new SubTypesScanner(false));
        return new HashSet<>(reflections.getSubTypesOf(Object.class));
    }

    public void setWebPageFactory(WebDriver driver){
        Set<Class> pageClass = findAllClassesUsingReflectionsLibrary();
        try{
            for(Class c : pageClass){
                PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(30)), Class.forName(c.getName()).newInstance());
            }
        }catch (Exception ignored) {
        }
    }

    public void setMobilePageFactory(AppiumDriver driver) {
        Set<Class> pageClass = findAllClassesUsingReflectionsLibrary();
        try{
            for(Class c : pageClass){
                PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(30)), Class.forName(c.getName()).newInstance());
            }
        }catch (Exception ignored) {
        }
    }
}