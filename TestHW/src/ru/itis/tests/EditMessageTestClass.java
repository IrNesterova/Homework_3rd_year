package ru.itis.tests;

import org.junit.Test;
import org.openqa.selenium.By;
import ru.itis.models.AccountData;
import ru.itis.models.MessageData;

public class EditMessageTestClass extends TestBase {
    @Test
    public void editPost(){
        app.getNavigationHelper().openHomePage();
        AccountData accountData = new AccountData("Avartia", "11c8945f");
        app.getLoginHelper().login(accountData);
       app.getNavigationHelper().openDiaryPage();
       MessageData messageData = new MessageData("лавлавыа", "fdfsd");
       app.getMessageHelper().editPost(messageData);
    }

}
