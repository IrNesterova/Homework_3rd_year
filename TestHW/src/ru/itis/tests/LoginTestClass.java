package ru.itis.tests;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.itis.models.AccountData;

public class LoginTestClass extends TestBase {

    @Test
    public void loginTest() {
        AccountData acc = new AccountData("Avartia", "11c8945f");
        String element = app.getWebDriver().getCurrentUrl();
        app.getLoginHelper().login(acc);
        app.getNavigationHelper().openHomePage();
        app.Wait();
        String element1 = app.getWebDriver().getCurrentUrl();
        Assert.assertEquals(element, element1);

    }



}
