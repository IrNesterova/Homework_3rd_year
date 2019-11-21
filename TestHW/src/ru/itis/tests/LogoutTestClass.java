package ru.itis.tests;

import org.junit.Test;
import org.openqa.selenium.By;

public class LogoutTestClass extends TestBase {
    @Test
    public void logoutTest(){
        app.getNavigationHelper().openHomePage();
        app.getLoginHelper().logout();
    }
}
