package ru.itis;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.itis.helpers.LoginHelper;
import ru.itis.helpers.MessageHelper;
import ru.itis.helpers.NavigationHelper;

import java.util.concurrent.TimeUnit;

public class ApplicationManager {
    private WebDriver webDriver;
    private static ThreadLocal<ApplicationManager> app = new ThreadLocal<>(){
        @Override
        protected ApplicationManager initialValue() {
            return super.initialValue();
        }

        @Override
        public ApplicationManager get() {
            return super.get();
        }
    };


    private LoginHelper loginHelper;
    private MessageHelper messageHelper;
    private NavigationHelper navigationHelper;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public LoginHelper getLoginHelper() {
        return loginHelper;
    }

    public MessageHelper getMessageHelper() {
        return messageHelper;
    }

    public NavigationHelper getNavigationHelper() {
        return navigationHelper;
    }

    private String baseURL;
    public ApplicationManager(){
        webDriver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver","c:\\games\\chromedriver.exe");
        baseURL = "http://diary.ru";
        loginHelper = new LoginHelper(this);
        messageHelper = new MessageHelper(this);
        navigationHelper = new NavigationHelper(this, baseURL);
    }
    public void Wait(){
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    public void Stop(){
        webDriver.quit();
    }
}
