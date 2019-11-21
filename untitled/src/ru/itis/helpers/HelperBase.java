package ru.itis.helpers;

import org.openqa.selenium.WebDriver;
import ru.itis.ApplicationManager;

public class HelperBase {
    public HelperBase(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
        this.webDriver = applicationManager.getWebDriver();
    }
    protected WebDriver webDriver;
    protected ApplicationManager applicationManager;


}
