package com.geekbrains.geekspring;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

public class SeleniumTest {
    private WebDriver driver;

    @BeforeSuite
    public void init() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
    }

    @Test
    public void test1() {
        driver.get("http://127.0.0.1:8181");

        WebElement webElement = driver.findElement(By.cssSelector(".nav-link"));
        webElement.click();

        List<WebElement> answerBlocks = driver
                .findElement(By.cssSelector("table"))
                .findElements(By.cssSelector("a"));

        System.out.println(answerBlocks.toString());
        Assert.assertEquals(answerBlocks.size(), 1);
    }

    @AfterSuite
    public void shutdown() {
        this.driver.quit();
    }
}
