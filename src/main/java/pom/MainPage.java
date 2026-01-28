package pom;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {
    private final WebDriver webDriver;
    public MainPage (WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    private final String url = "https://stellarburgers.education-services.ru/";

    // Локатор кнопки личный кабинет
    private final By accountButtonHeader = By.xpath(".//p[text()='Личный Кабинет']");
    // Локатор кнопки Войти в аккаунт
    private final By loginButton = By.xpath(".//button[text()='Войти в аккаунт']");

    @Step("Нажать на кнопку 'Личный кабинет' в шапке")
    public void clickAccountButtonHeader() {
        webDriver.findElement(accountButtonHeader).click();
    }

    @Step("Нажать на кнопку 'Войти в аккаунт'")
    public void clickLoginButton() {
        webDriver.findElement(loginButton).click();
    }
    public void open() {
        webDriver.get(url);
    }

}
