import Steps.UserSteps;
import com.github.javafaker.Faker;
import generator.UserExample;
import io.restassured.response.Response;
import model.UserRegister;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserCredsTests {
    private UserSteps steps;
    private UserRegister userRegister;
    private String accessToken;
    private String refreshToken;
    private Response response;
    private static final Faker faker = new Faker();


    @BeforeEach
    public void setUp() {
        steps = new UserSteps();
        userRegister = UserExample.builder().build();
        response = steps.registerUser(userRegister);
        steps.printResponseBody(response);
        accessToken = steps.extractAccessTokenFromResponse(response);
        refreshToken = steps.extractRefreshTokenFromResponse(response);
    }

    @Test
    @DisplayName("изменение Email пользователя с авторизацией")
    public void updateEmailWithAuthTest() {
        String newEmail = faker.internet().emailAddress();
        UserRegister updatedUser = new UserRegister(newEmail,
                userRegister.getPassword(),
                userRegister.getName());
        Response response1 = steps.updateUser(accessToken, updatedUser);
        steps.printResponseBody(response1);
        assertEquals(SC_OK, response1.statusCode(),
                "должен возвращаться код 200");
        assertTrue(response1.jsonPath().getBoolean("success"),
                "ответ должен содержать success: true");
        assertNotNull(response1.jsonPath().get("user"),
                "В ответе должно быть поле 'user'");
        assertEquals(newEmail, response1.jsonPath().getString("user.email"),
                "Email в ответе должен совпадать с отправленным");
        assertEquals(userRegister.getName(), response.jsonPath().getString("user.name"),
                "Имя в ответе должно остаться прежним");
        String message = response1.jsonPath().getString("message");
        assertNull(message, "При успешном обновлении не должно быть сообщения об ошибке");
    }
    @Test
    @DisplayName("изменение Email пользователя без авторизации")
    public void updateEmailWithoutAuthTest() {

        UserRegister updatedUser = new UserRegister(faker.internet().emailAddress(),
                userRegister.getPassword(),
                userRegister.getName());
        Response response1 = steps.updateUserWithoutAuth(updatedUser);
        steps.printResponseBody(response1);
        assertEquals(SC_UNAUTHORIZED, response1.statusCode(),
                "должен возвращаться код 401");
        assertFalse(response1.jsonPath().getBoolean("success"),
                "ответ должен содержать success: false");
        assertEquals("You should be authorised", response1.jsonPath().getString("message"),
                "Сообщение должно быть 'You should be authorised'");
    }
    @Test
    @DisplayName("изменение пароля пользователя c авторизацией")
    public void updatePasswordWithAuthTest() {
        String newPassword = faker.internet().password();
        UserRegister updatedUser2 = new UserRegister(userRegister.getEmail(),
                newPassword,
                userRegister.getName());
        Response response1 = steps.updateUser(accessToken, updatedUser2);
        steps.printResponseBody(response1);
        assertEquals(SC_OK, response1.statusCode(),
                "должен возвращаться код 200");
        assertTrue(response1.jsonPath().getBoolean("success"),
                "ответ должен содержать success: true");
        assertNotNull(response1.jsonPath().get("user"),
                "В ответе должно быть поле 'user'");
        assertEquals(userRegister.getName(), response.jsonPath().getString("user.name"),
                "Имя в ответе должно остаться прежним");
        String message = response1.jsonPath().getString("message");
        assertNull(message, "При успешном обновлении не должно быть сообщения об ошибке");

    }
    @Test
    @DisplayName("изменение пароля пользователя без авторизации")
    public void updatePasswordWithoutAuthTest() {
        UserRegister updatedUser2 = new UserRegister(userRegister.getEmail(),
                faker.internet().password(),
                userRegister.getName());
        Response response2 = steps.updateUserWithoutAuth(updatedUser2);
        steps.printResponseBody(response2);
        assertEquals(SC_UNAUTHORIZED, response2.statusCode(),
                "должен возвращаться код 401");
        assertFalse(response2.jsonPath().getBoolean("success"),
                "ответ должен содержать success: false");
        assertEquals("You should be authorised", response2.jsonPath().getString("message"),
                "Сообщение должно быть 'You should be authorised'");
    }
    @Test
    @DisplayName("изменение имени пользователя c авторизацией")
    public void updateNameWithAuthTest() {
        String newName = faker.name().fullName();
        UserRegister updatedUser3 = new UserRegister(userRegister.getEmail(),
                userRegister.getPassword(),
                newName);
        Response response3 = steps.updateUser(accessToken, updatedUser3);
        steps.printResponseBody(response3);
        assertEquals(SC_OK, response3.statusCode(),
                "должен возвращаться код 200");
        assertTrue(response3.jsonPath().getBoolean("success"),
                "ответ должен содержать success: true");
        assertNotNull(response3.jsonPath().get("user"),
                "В ответе должно быть поле 'user'");
        assertEquals(userRegister.getEmail(), response3.jsonPath().getString("user.email"),
                "Email в ответе должен совпадать с отправленным");
        assertEquals(newName, response3.jsonPath().getString("user.name"),
                "Имя в ответе должно остаться прежним");
        String message = response3.jsonPath().getString("message");
        assertNull(message, "При успешном обновлении не должно быть сообщения об ошибке");
    }

    @Test
    @DisplayName("изменение имени пользователя без авторизации")
    public void updateNameWithoutAuthTest() {
        UserRegister updatedUser3 = new UserRegister(userRegister.getEmail(),
                userRegister.getPassword(),
                faker.name().fullName());
        Response response3 = steps.updateUserWithoutAuth(updatedUser3);
        steps.printResponseBody(response3);
        assertEquals(SC_UNAUTHORIZED, response3.statusCode(),
                "должен возвращаться код 401");
        assertFalse(response3.jsonPath().getBoolean("success"),
                "ответ должен содержать success: false");
        assertEquals("You should be authorised", response3.jsonPath().getString("message"),
                "Сообщение должно быть 'You should be authorised'");
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
