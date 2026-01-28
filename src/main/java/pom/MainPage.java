package pom;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {
    private final WebDriver webDriver;
    public MainPage (WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public static String url = "https://stellarburgers.education-services.ru/";

    // Локатор кнопки личный кабинет
    private final By accountButtonHeader = By.xpath(".//p[text()='Личный Кабинет']");
    // Локатор кнопки Войти в аккаунт
    private final By loginButton = By.xpath(".//button[text()='Войти в аккаунт']");
    // Локатор кнопки Оформить заказ
    private final By orderCreateButton = By.xpath("//button[text()='Оформить заказ']");


    // открыть главную страницу
    public MainPage open() {
        webDriver.get(url);
        return this;
    }

    @Step("Нажать на кнопку 'Личный кабинет' в шапке")
    public LoginPage clickAccountButtonHeader() {
        webDriver.findElement(accountButtonHeader).click();
        return new LoginPage(webDriver);
    }

    @Step("Нажать на кнопку 'Войти в аккаунт'")
    public LoginPage clickLoginButton() {
        webDriver.findElement(loginButton).click();
        return new LoginPage(webDriver);
    }
    // метод получения кнопки оформить заказ
    public Boolean isBottonExsist() {
        try {
            webDriver.findElement(orderCreateButton);
            return true;
        } catch (Exception exception) {
            return false; // Элемент не найден - неудача
        }
    }

}
