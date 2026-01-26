import Steps.UserSteps;
import generator.UserExample;
import io.restassured.response.Response;
import model.UserRegister;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTests {


    private UserSteps steps;
    private UserRegister userRegister;
    private String accessToken;
    private Response response;

    @BeforeEach
    public void setUp() {
        steps = new UserSteps();
        userRegister = UserExample.builder().build();
        response = steps.registerUser(userRegister);
        steps.printResponseBody(response);
        accessToken = steps.extractAccessTokenFromResponse(response);
    }

    @Test
    @DisplayName("авторизация пользователя")
    public void loginUserTest() {

        steps.loginUser(accessToken, userRegister);
        Response responseSecond = steps.loginUser(accessToken, userRegister);
        steps.printResponseBody(responseSecond);
        assertEquals(SC_OK, responseSecond.statusCode(),
                "При успешном создании должен возвращаться код 200");
        assertTrue(responseSecond.jsonPath().getBoolean("success"),
                "ответ должен содержать success: true");


    }
    @Test
    @DisplayName("авторизация пользователя c неверными данными")
    public void loginUserInvalidCredsTest() {
        // Изменяем email
        String modifiedEmail = userRegister.getEmail() + "1";
        UserRegister modifiedEmailUser = new UserRegister(
                modifiedEmail,
                userRegister.getPassword(),
                userRegister.getName()
        );

        steps.loginUser(accessToken, modifiedEmailUser);
        Response responseSecond = steps.loginUser(accessToken, modifiedEmailUser);
        steps.printResponseBody(responseSecond);
        assertEquals(SC_UNAUTHORIZED, responseSecond.statusCode(),
                "Должен возвращаться код 401");
        steps.printResponseBody(responseSecond);
        assertFalse(responseSecond.jsonPath().getBoolean("success"),
                "ответ должен содержать success: false");
        assertEquals("email or password are incorrect", responseSecond.jsonPath().getString("message") ,
                "Сообщение должно быть 'email or password are incorrect'");
        // Изменяем Password
        String modifiedPassword = userRegister.getPassword() + "1";
        UserRegister modifiedPasswordUser = new UserRegister(
                userRegister.getEmail(),
                modifiedPassword,
                userRegister.getName()
        );
        steps.loginUser(accessToken, modifiedPasswordUser);
        Response response3 = steps.loginUser(accessToken, modifiedPasswordUser);
        steps.printResponseBody(response3);
        assertEquals(SC_UNAUTHORIZED, response3.statusCode(),
                "Должен возвращаться код 401");
        steps.printResponseBody(responseSecond);
        assertFalse(responseSecond.jsonPath().getBoolean("success"),
                "ответ должен содержать success: false");
        assertEquals("email or password are incorrect", responseSecond.jsonPath().getString("message") ,
                "Сообщение должно быть 'email or password are incorrect'");

    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            try {
                steps.deleteUser(accessToken);
                System.out.println("Пользователь успешно удален");
            } catch (Exception e) {
                System.err.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        } else {
            System.out.println("AccessToken отсутствует, удаление пропущено");
        }
    }
}
