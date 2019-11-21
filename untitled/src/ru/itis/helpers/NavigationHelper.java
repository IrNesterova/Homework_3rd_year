package ru.itis.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.itis.ApplicationManager;
import ru.itis.helpers.HelperBase;

public class NavigationHelper extends HelperBase {
    public NavigationHelper(ApplicationManager manager, String baseURL){
        super(manager);
        this.baseURL = baseURL;
    }
    private String baseURL;

    public void openHomePage(){
        webDriver.get(baseURL);
    }
    public void openMessagePage(){
        webDriver.findElement(By.linkText("Новая запись")).click();
    }

    public void openDiaryPage(){
        webDriver.findElement(By.linkText("Мой дневник")).click();
    }
}
