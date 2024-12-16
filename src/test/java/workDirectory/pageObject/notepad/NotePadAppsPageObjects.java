package workDirectory.pageObject.notepad;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class NotePadAppsPageObjects {
    @AndroidFindBy(id = "new_note_fab")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Continue\"]")
    public static WebElement newNoteButton;

    @AndroidFindBy(id = "textNoteTitleEdit")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Allow While Using App\"]")
    public static WebElement noteTitleInput;

    @AndroidFindBy(id = "textNoteContentEdit")
    public static WebElement noteBodyInput;

    @AndroidFindBy(id = "action_save_note")
    public static WebElement saveButton;

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc='Navigate up']")
    public static WebElement backButton;

    @AndroidFindBy(id = "note_list_title_text")
    public static List<WebElement> listNoteTitle;

}
