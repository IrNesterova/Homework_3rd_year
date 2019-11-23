package ru.itis.tests;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.itis.models.AccountData;
import ru.itis.models.MessageData;

import java.util.concurrent.TimeUnit;

public class CreateMessageTestClass extends TestBase {
    @Test
    public void creatingEntitiesTest() {
        AccountData accountData = new AccountData("Avartia", "11c8945f");
        app.getLoginHelper().login(accountData);
        app.Wait();
        app.getNavigationHelper().openDiaryPage();
        WebElement element1 = app.getWebDriver().findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]"));
        app.getNavigationHelper().openMessagePage();
        app.Wait();
        MessageData messageData = new MessageData("привет", "я делаю автоматизированное тестирование");
        app.getMessageHelper().creatingPost(messageData);
        app.Wait();
        WebElement element2 = app.getWebDriver().findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]"));
        Assert.assertNotEquals(element1, element2);
    }
}
