import extension.BrowserExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pom.LoginPage;
import pom.MainPage;

import java.time.Duration;

import static java.time.Duration.ofSeconds;

public class SucessRegisterTests {

    private MainPage mainPage;
    @RegisterExtension
    public BrowserExtension browserExtension = new BrowserExtension();

@Test
public void testSuccessRegisterHeader() {
    MainPage objHomePage = new MainPage(browserExtension.getWebDriver());
    LoginPage objLoginPage = new LoginPage(browserExtension.getWebDriver());
    objHomePage.open();
    objHomePage.clickAccountButtonHeader();
    objLoginPage.clickRegisterLink();

}
    @Test
    public void testSuccessRegisterAccount() {
        MainPage objHomePage = new MainPage(browserExtension.getWebDriver());
        LoginPage objLoginPage = new LoginPage(browserExtension.getWebDriver());
        objHomePage.open();
        objHomePage.clickLoginButton();
        objLoginPage.clickRegisterLink();


    }

}
