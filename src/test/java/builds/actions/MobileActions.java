package builds.actions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

public class MobileActions extends MainActions {

    public void hideKeyboard(){
        if (driver.get() instanceof AndroidDriver) {
            ((AndroidDriver) driver.get()).hideKeyboard();
        } else if (driver.get() instanceof IOSDriver) {
            ((IOSDriver) driver.get()).hideKeyboard();
        } else {
            System.out.println("Driver is not an instance of AndroidDriver or IOSDriver.");
        }
    }

    public enum SwipeDirection {
        UP, DOWN, LEFT, RIGHT
    }

    public void swipe(SwipeDirection direction, int percentage) {
        int screenWidth = driver.get().manage().window().getSize().width;
        int screenHeight = driver.get().manage().window().getSize().height;

        int startX, startY, endX, endY;

        switch (direction) {
            case UP:
                startX = screenWidth / 2;
                startY = (int) (screenHeight * (1 - (percentage / 100.0)));
                endX = startX;
                endY = (int) (screenHeight * 0.1);  // Near the top
                break;

            case DOWN:
                startX = screenWidth / 2;
                startY = (int) (screenHeight * (percentage / 100.0));
                endX = startX;
                endY = (int) (screenHeight * 0.9);  // Near the bottom
                break;

            case LEFT:
                startX = (int) (screenWidth * (1 - (percentage / 100.0)));
                startY = screenHeight / 2;
                endX = (int) (screenWidth * 0.1);
                endY = startY;
                break;

            case RIGHT:
                startX = (int) (screenWidth * (percentage / 100.0));
                startY = screenHeight / 2;
                endX = (int) (screenWidth * 0.9);
                endY = startY;
                break;

            default:
                throw new IllegalArgumentException("Invalid swipe direction! Use: UP, DOWN, LEFT, or RIGHT.");
        }

        performSwipe(startX, startY, endX, endY);
    }

    private void performSwipe(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((AppiumDriver) driver.get()).perform((Collections.singletonList(swipe)));
    }

     void swipeDownToElement(String elementName, Integer timeout){
        boolean found = false;
        while(!found){

//           if(verifyElementVisible(elementName, timeout)==false){
//
//           }
        }

    }

     void swipeUpToElement(String elementName, Integer timeout){

    }

    public void pressEnter() {
        if(driver.get() instanceof IOSDriver){
            driver.get().findElement(By.xpath("//XCUIElementTypeButton[@name=\"Go\"]")).click();
        }else if (driver.get() instanceof AndroidDriver){
            ((AndroidDriver) driver.get()).pressKey(new KeyEvent(AndroidKey.ENTER));
        }else {
            System.out.println("Driver is not an instance of AndroidDriver or IOSDriver.");
        }
    }
}
