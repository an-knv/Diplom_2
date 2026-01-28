package edu.praktikum.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverCreator {


    //Переменные окружения, прописанные в системе:
    public static String YANDEX_BROWSER_DRIVER_FILENAME = "yandexdriver";//- имя файла драйвера Яндекс браузера (Хромдрайвера нужной версии)
    public static String YANDEX_BROWSER_PATH = "/Applications/Yandex.app/Contents/MacOS/Yandex";


    public static WebDriver createWebDriver() {
        String browser = System.getProperty("browser");
        if (browser == null) {
            return createChromeDriver();
        }

        switch (browser) {
            case "yandex":
                return createYandexDriver();
            case "chrome":
            default:
                return createChromeDriver();
        }
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        return new ChromeDriver(options);
    }

    private static WebDriver createYandexDriver() {
        System.setProperty("webdriver.chrome.driver",
                String.format("%s/%s", System.getenv("WEBDRIVERS"),
                        YANDEX_BROWSER_DRIVER_FILENAME));
        ChromeOptions options = new ChromeOptions();
        options.setBinary(YANDEX_BROWSER_PATH);
        return new ChromeDriver(options);
    }
}