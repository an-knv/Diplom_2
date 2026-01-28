package pom;

import generator.UserExample;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver webDriver;
    public LoginPage (WebDriver webDriver) {
        this.webDriver = webDriver;
    }
    public static String LOGIN_URL = "https://stellarburgers.education-services.ru/login";

    // Локатор ссылки Зарегистрироваться
    private final By registerLink = By.xpath("//a[text()='Зарегистрироваться']");
    private final By emailField = By.xpath("//input[@name='name']");
    private final By passwordField = By.xpath("//input[@name='Пароль']");
    private final By enterButton = By.xpath("//button[text()='Войти']");

    @Step("Нажать на ссылку 'Зарегистрироваться'")
    public void clickRegisterLink() {
        webDriver.findElement(registerLink).click();
    }

    @Step("Проверить что страница открылась")
    public boolean isUrlCorrect() {
        String currentUrl = webDriver.getCurrentUrl();
        return currentUrl.contains("register");
    }
    @Step("Заполняем поля авторизации")
    public LoginPage enterAuthFields(UserExample user) {
        webDriver.findElement(emailField).sendKeys(user.getEmail());
        webDriver.findElement(passwordField).sendKeys(user.getPassword());
        return this;
    }
    @Step("Нажать на кнопку 'Войти'")
    public void clickEnterButton() {
        webDriver.findElement(enterButton).click();
    }

}
