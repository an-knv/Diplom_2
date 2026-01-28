import dev.failsafe.internal.util.Assert;
import extension.BrowserExtension;
import generator.UserExample;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pom.LoginPage;
import pom.MainPage;
import pom.RegisterPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static generator.UserExample.faker;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SucessRegisterTests {

    private MainPage mainPage;
    @RegisterExtension
    public BrowserExtension browserExtension = new BrowserExtension();


@Test
@DisplayName("успешная регистрация пользователя из хедера")
public void testSuccessRegisterHeader() {
    UserExample newUser = UserExample.randomUser();
    MainPage objHomePage = new MainPage(browserExtension.getWebDriver());
    LoginPage objLoginPage = new LoginPage(browserExtension.getWebDriver());
    RegisterPage objRegisterPage = new RegisterPage(browserExtension.getWebDriver());
    objHomePage.open()
                .clickAccountButtonHeader();
    objLoginPage.clickRegisterLink();
    objRegisterPage.enterRegisterFields(newUser)
            .clickRegisterButton();
    WebDriverWait wait = new WebDriverWait(browserExtension.getWebDriver(), Duration.ofSeconds(1));
    wait.until(ExpectedConditions.urlToBe(LoginPage.LOGIN_URL));
    objLoginPage.enterAuthFields(newUser)
            .clickEnterButton();
    new WebDriverWait(browserExtension.getWebDriver(), Duration.ofSeconds(3));
    assertTrue(objHomePage.isBottonExsist());

}
    @Test
    @DisplayName("успешная регистрация пользователя по кнопку войти в аккаунт")
    public void testSuccessRegisterAccount() {
        UserExample newUser = UserExample.randomUser();
        MainPage objHomePage = new MainPage(browserExtension.getWebDriver());
        LoginPage objLoginPage = new LoginPage(browserExtension.getWebDriver());
        RegisterPage objRegisterPage = new RegisterPage(browserExtension.getWebDriver());
        objHomePage.open()
                   .clickLoginButton();
        objLoginPage.clickRegisterLink();
        objRegisterPage.enterRegisterFields(newUser)
                        .clickRegisterButton();
        WebDriverWait wait = new WebDriverWait(browserExtension.getWebDriver(), Duration.ofSeconds(1));
        wait.until(ExpectedConditions.urlToBe(LoginPage.LOGIN_URL));
        objLoginPage.enterAuthFields(newUser)
                .clickEnterButton();
        new WebDriverWait(browserExtension.getWebDriver(), Duration.ofSeconds(3));
        assertTrue(objHomePage.isBottonExsist());
    }
    @Test
    @DisplayName("Ошибку для некорректного пароля. Минимальный пароль — шесть символов.")
    public void testWrongPassword() {
        UserExample newUser = UserExample.userWrongPassword();
        MainPage objHomePage = new MainPage(browserExtension.getWebDriver());
        LoginPage objLoginPage = new LoginPage(browserExtension.getWebDriver());
        RegisterPage objRegisterPage = new RegisterPage(browserExtension.getWebDriver());
        objHomePage.open()
                .clickLoginButton();
        objLoginPage.clickRegisterLink();
        objRegisterPage.enterRegisterFields(newUser)
                .clickRegisterButton();
        assertTrue(objRegisterPage.isWrongPasswordMessageExsist());
    }
}
