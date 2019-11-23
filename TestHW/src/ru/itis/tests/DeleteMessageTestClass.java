package ru.itis.tests;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.itis.models.AccountData;

public class DeleteMessageTestClass extends TestBase{
    @Test
    public void deletePost(){
        app.getNavigationHelper().openHomePage();
        AccountData accountData = new AccountData("Avartia", "11c8945f");
        app.getLoginHelper().login(accountData);
        app.Wait();
        app.getNavigationHelper().openDiaryPage();
        WebElement element1 = app.getWebDriver().findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]"));

        app.getMessageHelper().deletePost();
        app.Wait();
        WebElement element2 = app.getWebDriver().findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]"));
        Assert.assertNotEquals(element1, element2);
    }
}
