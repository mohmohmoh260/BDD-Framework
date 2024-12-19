package builds.utilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageFactoryInit {

    private List<Map<String, String>> globalParameters = null;

    @BeforeSuite
    private void setGlobalParameters(){
        TestNGXmlParser testNGXmlParser = new TestNGXmlParser();
        globalParameters = testNGXmlParser.getGlobalParametersOnly();
    }

    protected List<Map<String, String>> getGlobalParameters(){
        return globalParameters;
    }

    private Set<Class> findAllClassesUsingReflectionsLibrary() {
        Reflections reflections = new Reflections("workDirectory.pageObject", new SubTypesScanner(false));
        return new HashSet<>(reflections.getSubTypesOf(Object.class));
    }

    protected void setWebPageFactory(WebDriver driver){
        Set<Class> pageClass = findAllClassesUsingReflectionsLibrary();
        try{
            for(Class c : pageClass){
                PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(30)), Class.forName(c.getName()).newInstance());
            }
        }catch (Exception ignored) {
        }
    }

    protected void setMobilePageFactory(AppiumDriver driver) {
        Set<Class> pageClass = findAllClassesUsingReflectionsLibrary();
        try{
            for(Class c : pageClass){
                PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(30)), Class.forName(c.getName()).newInstance());
            }
        }catch (Exception ignored) {
        }
    }
}
