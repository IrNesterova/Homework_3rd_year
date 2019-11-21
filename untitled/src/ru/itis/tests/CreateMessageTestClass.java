package ru.itis.tests;

import org.junit.Test;
import org.openqa.selenium.By;
import ru.itis.models.MessageData;

public class CreateMessageTestClass extends TestBase {
    @Test
    public void creatingEntitiesTest() {
        app.getNavigationHelper().openDiaryPage();
        app.getNavigationHelper().openMessagePage();
        MessageData messageData = new MessageData("привет", "я делаю автоматизированное тестирование");
        app.getMessageHelper().creatingPost(messageData);
    }
}
