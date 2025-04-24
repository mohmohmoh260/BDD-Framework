package builds.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VisualUITest {

    public static void visualTest() throws Exception {
        WebDriver driver = new ChromeDriver();
        driver.get("https://example.com");

        // Take current screenshot
        Screenshot actualScreenshot = new AShot().takeScreenshot(driver);
        BufferedImage actualImage = actualScreenshot.getImage();

        // Load baseline image
        BufferedImage expectedImage = ImageIO.read(new File("baseline/example_page.png"));

        // Compare images
        ImageDiff diff = new ImageDiffer().makeDiff(expectedImage, actualImage);

        if (diff.hasDiff()) {
            System.out.println("❌ UI has changed!");
            ImageIO.write(actualImage, "PNG", new File("diffs/actual.png"));
        } else {
            System.out.println("✅ UI matches baseline.");
        }

        driver.quit();
    }
}