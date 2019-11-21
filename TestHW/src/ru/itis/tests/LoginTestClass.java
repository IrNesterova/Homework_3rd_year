package ru.itis.tests;

import org.junit.Test;
import org.openqa.selenium.By;
import ru.itis.models.AccountData;

public class LoginTestClass extends TestBase {

    @Test
    public void loginTest() {
        app.getNavigationHelper().openHomePage();
        AccountData acc = new AccountData("Avartia", "11c8945f");
        app.getLoginHelper().login(acc);
        app.getNavigationHelper().openHomePage();
    }



}
