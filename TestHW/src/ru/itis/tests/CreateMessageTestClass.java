package ru.itis.tests;

import org.junit.Test;
import org.openqa.selenium.By;
import ru.itis.models.AccountData;
import ru.itis.models.MessageData;

import java.util.concurrent.TimeUnit;

public class CreateMessageTestClass extends TestBase {
    @Test
    public void creatingEntitiesTest() {
        app.getNavigationHelper().openHomePage();
        AccountData accountData = new AccountData("Avartia", "11c8945f");
        app.getLoginHelper().login(accountData);
        app.Wait();
        app.getNavigationHelper().openDiaryPage();
        app.getNavigationHelper().openMessagePage();
        app.Wait();
        MessageData messageData = new MessageData("привет", "я делаю автоматизированное тестирование");
        app.getMessageHelper().creatingPost(messageData);
        app.Wait();
    }
}
