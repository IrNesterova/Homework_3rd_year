package ru.itis.tests;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.itis.models.AccountData;
import ru.itis.models.MessageData;

public class EditMessageTestClass extends TestBase {
    @Test
    public void editPost(){
        app.getNavigationHelper().openHomePage();
        AccountData accountData = new AccountData("Avartia", "11c8945f");
        app.getLoginHelper().login(accountData);
        app.Wait();
        WebElement element1 = app.getWebDriver().findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]"));
        app.getNavigationHelper().openDiaryPage();
        MessageData messageData = new MessageData("лавлавыа", "fdfsd");
        app.getMessageHelper().editPost(messageData);
        app.Wait();
        WebElement element = app.getWebDriver().findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]"));
        Assert.assertNotEquals(element, element1);
    }

}
