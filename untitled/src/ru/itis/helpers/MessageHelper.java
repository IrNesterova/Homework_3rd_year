package ru.itis.helpers;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.itis.ApplicationManager;
import ru.itis.helpers.HelperBase;
import ru.itis.models.MessageData;

public class MessageHelper extends HelperBase {
    public MessageHelper(ApplicationManager manager){
        super(manager);
    }

    public void editPost(MessageData editMessage){
        webDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]/div[2]/ul/li[3]/a")).click();
        webDriver.findElement(By.id("postTitle")).sendKeys(editMessage.getPostTitle());
        webDriver.findElement(By.id("message")).sendKeys(editMessage.getMessage());
        webDriver.findElement(By.id("rewrite")).click();
    }
    public void deletePost(){
        webDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[3]/div[2]/ul/li[2]/a")).click();
        webDriver.findElement(By.name("yes")).click();
    }
    public void creatingPost(MessageData message) {

        webDriver.findElement(By.id("postTitle")).sendKeys(message.getPostTitle());
        webDriver.findElement(By.id("message")).sendKeys(message.getMessage());
        webDriver.findElement(By.id("rewrite")).click();
    }

}
