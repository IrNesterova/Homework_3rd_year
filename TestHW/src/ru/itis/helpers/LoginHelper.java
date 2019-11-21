package ru.itis.helpers;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.itis.ApplicationManager;
import ru.itis.helpers.HelperBase;
import ru.itis.models.AccountData;

public class LoginHelper extends HelperBase {
    public LoginHelper(ApplicationManager manager){
        super(manager);
    }

    public void login(AccountData user) {
        webDriver.findElement(By.name("user_login")).sendKeys(user.getUsername());
        webDriver.findElement(By.name("user_pass")).sendKeys(user.getPassword());
        webDriver.findElement(By.id("save")).click();
        webDriver.findElement(By.className("submit")).click();
    }

    public void logout(){
        webDriver.findElement(By.linkText("Выход")).click();
    }
}
