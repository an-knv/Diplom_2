import Steps.UserSteps;
import generator.UserExample;
import io.restassured.response.Response;
import model.UserCreds;
import model.UserRegister;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserRegisterTests {

    private UserSteps steps;
    private UserRegister userRegister;
    private String accessToken;
    private Response response;

    @BeforeEach
    public void setUp() {
        steps = new UserSteps();
    }

    @Test
    @DisplayName("создание уникального пользователя")
    public void createUniqueUserTest() {

        userRegister = UserExample.builder().build();
        // Регистрируем пользователя
        response = steps.registerUser(userRegister);
        //проверяем ответ
        assertEquals(SC_OK, response.statusCode(),
                "При успешном создании должен возвращаться код 200");
        UserCreds creds = response.as(UserCreds.class);
        steps.printResponseBody(response);
        assertTrue(creds.isSuccess(),
                "ответ должен содержать success: true");
        accessToken = steps.extractAccessTokenFromResponse(response);
        System.out.println("Access Token: " + accessToken);

    }

    @Test
    @DisplayName("создание пользователя, который уже зарегистрирован")
    public void createTheSameUserTest() {

        userRegister = UserExample.builder().build();
        response = steps.registerUser(userRegister);
        assertEquals(SC_OK, response.statusCode(),
                "При успешном создании должен возвращаться код 200");
        steps.printResponseBody(response);
        UserCreds creds = response.as(UserCreds.class);
        assertTrue(creds.isSuccess(),
                "ответ должен содержать success: true");
        accessToken = steps.extractAccessTokenFromResponse(response);
        // Регистрируем пользователя повторно
        Response responseSecond = steps.registerUser(userRegister);
        steps.printResponseBody(responseSecond);
        UserCreds creds2 = responseSecond.as(UserCreds.class);
        assertEquals(SC_FORBIDDEN, responseSecond.statusCode(),
                "При повторном создании должен возвращаться код 403");
        assertFalse(creds2.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals("User already exists", creds2.getMessage(),
                "Сообщение должно быть 'User already exists'");

    }
    @Test
    @DisplayName("создание пользователя без email")
    public void createUserWithoutEmailTest() {

        userRegister = UserExample.builder().build();
        userRegister.setEmail(null);
        response = steps.registerUser(userRegister);
        assertEquals(SC_FORBIDDEN, response.statusCode(),
                "При создании должен возвращаться код 403");
        steps.printResponseBody(response);
        UserCreds creds = response.as(UserCreds.class);
        assertFalse(creds.isSuccess(),
                "ответ должен содержать success: false");
        accessToken = steps.extractAccessTokenFromResponse(response);
        assertEquals("Email, password and name are required fields", creds.getMessage(),
                "Сообщение должно быть 'Email, password and name are required fields'");

    }
    @Test
    @DisplayName("создание пользователя без пароля")
    public void createUserWithoutPasswordTest() {

        userRegister = UserExample.builder().build();
        userRegister.setPassword(null);
        response = steps.registerUser(userRegister);
        assertEquals(SC_FORBIDDEN, response.statusCode(),
                "При создании должен возвращаться код 403");
        steps.printResponseBody(response);
        UserCreds creds = response.as(UserCreds.class);
        assertFalse(creds.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals("Email, password and name are required fields", creds.getMessage(),
                "Сообщение должно быть 'Email, password and name are required fields'");

    }

    @Test
    @DisplayName("создание пользователя без имени")
    public void createUserWithoutNameTest() {

        userRegister = UserExample.builder().build();
        userRegister.setName(null);
        response = steps.registerUser(userRegister);
        assertEquals(SC_FORBIDDEN, response.statusCode(),
                "При создании должен возвращаться код 403");
        steps.printResponseBody(response);
        UserCreds creds = response.as(UserCreds.class);
        assertFalse(creds.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals("Email, password and name are required fields", creds.getMessage(),
                "Сообщение должно быть 'Email, password and name are required fields'");

    }

    @AfterEach
    public void tearDown() {
        if(accessToken != null) {
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
