package ru.itis.tests;

import org.junit.Test;
import org.openqa.selenium.By;
import ru.itis.models.AccountData;

public class DeleteMessageTestClass extends TestBase{
    @Test
    public void deletePost(){
        app.getNavigationHelper().openHomePage();
        AccountData accountData = new AccountData("Avartia", "11c8945f");
        app.getLoginHelper().login(accountData);
        app.getNavigationHelper().openDiaryPage();
        app.getMessageHelper().deletePost();
    }
}
