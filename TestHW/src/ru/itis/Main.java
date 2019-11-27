package ru.itis;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class Main {
    WebDriver webDriver = new ChromeDriver();
    @BeforeClass
    public static void site_test_1() {
        System.setProperty("webdriver.chrome.driver","c:\\games\\chromedriver.exe");
    }

    @Test
    public void Login(){
        webDriver.get("http://diary.ru");
        webDriver.findElement(By.name("user_login")).sendKeys("Avartia");
        webDriver.findElement(By.name("user_pass")).sendKeys("11c8945f");
        webDriver.findElement(By.id("save")).click();
        webDriver.findElement(By.className("submit")).click();
    }

    @After
    public void TearDown(){
        webDriver.quit();
    }
}
