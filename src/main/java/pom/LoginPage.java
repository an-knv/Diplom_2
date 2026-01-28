package pom;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver webDriver;
    public LoginPage (WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    // Локатор ссылки Зарегистрироваться
    private final By registerLink = By.xpath("//a[text()='Зарегистрироваться']");

    @Step("Нажать на ссылку 'Зарегистрироваться'")
    public void clickRegisterLink() {
        webDriver.findElement(registerLink).click();
    }


}
