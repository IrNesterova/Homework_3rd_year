package ru.itis.tests;

import org.junit.Test;
import org.openqa.selenium.By;

public class DeleteMessageTestClass extends TestBase{
    @Test
    public void deletePost(){
        app.getNavigationHelper().openDiaryPage();
        app.getMessageHelper().deletePost();
    }
}
