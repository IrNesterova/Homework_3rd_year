package ru.itis.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.itis.ApplicationManager;
import ru.itis.models.AccountData;
import ru.itis.models.MessageData;

import java.util.concurrent.TimeUnit;

public class TestBase  {
    protected int time = 5;
    ApplicationManager app;
    @Before
    public void setUp(){
        app = ApplicationManager.getInstance();
    }

    @BeforeClass
    public static void site_test_1() {
        System.setProperty("webdriver.chrome.driver","c:\\games\\chromedriver.exe");
    }


    @After
    public void tearDown(){
        app.getWebDriver().quit();
    }

}
