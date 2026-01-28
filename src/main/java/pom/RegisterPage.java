package pom;

import generator.UserExample;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static generator.UserExample.faker;

public class RegisterPage {
    private final WebDriver webDriver;

    public RegisterPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    // Локаторы на странице
    private final By emailField = By.xpath("//label[text()='Email']/../input");
    private final By nameField= By.xpath("//input[@name='name']");
    private final By passwordField = By.xpath("//input[@name='Пароль']");
    private final By registerButton = By.xpath("//button[text()='Зарегистрироваться']");
    private final By wrongPasswordMessage = By.xpath("//p[text()='Некорректный пароль']");

    /*@Step("Заполняем email")
    public RegisterPage enterEmailField(String email) {
        webDriver.findElement(emailField).sendKeys(email);
        return this;
    }
    @Step("Заполняем имя")
    public RegisterPage enterNameField(String name) {
        webDriver.findElement(nameField).sendKeys(name);
        return this;
    }*/
    @Step("Заполняем поля регистрации")
    public RegisterPage enterRegisterFields(UserExample user) {
        webDriver.findElement(nameField).sendKeys(user.getName());
        webDriver.findElement(emailField).sendKeys(user.getEmail());
        webDriver.findElement(passwordField).sendKeys(user.getPassword());
        return this;
    }
    @Step("Нажать на кнопку 'Зарегистрироваться'")
    public void clickRegisterButton() {
        webDriver.findElement(registerButton).click();
    }
    // метод получения ошибки неверного пароля
    public Boolean isWrongPasswordMessageExsist() {
        try {
            webDriver.findElement(wrongPasswordMessage);
            return true;
        } catch (Exception exception) {
            return false; // Элемент не найден - неудача
        }
    }

}
