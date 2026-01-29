import Steps.UserSteps;
import com.github.javafaker.Faker;
import generator.UserExample;
import io.restassured.response.Response;
import model.UserCreds;
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
    private Response response;
    private static final Faker faker = new Faker();


    @BeforeEach
    public void setUp() {
        steps = new UserSteps();
        userRegister = UserExample.builder().build();
        response = steps.registerUser(userRegister);
        steps.printResponseBody(response);
        accessToken = steps.extractAccessTokenFromResponse(response);
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
        UserCreds creds = response1.as(UserCreds.class);
        assertEquals(SC_OK, response1.statusCode(),
                "должен возвращаться код 200");
        assertTrue(creds.isSuccess(),
                "ответ должен содержать success: true");
        assertNotNull(creds.getUser(),
                "В ответе должно быть поле 'user'");
        assertEquals(newEmail, creds.getUser().getEmail(),
                "Email в ответе должен совпадать с отправленным");
        assertEquals(userRegister.getName(), creds.getUser().getName(),
                "Имя в ответе должно остаться прежним");
        assertNull(creds.getMessage(), "При успешном обновлении не должно быть сообщения об ошибке");
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
        UserCreds creds = response1.as(UserCreds.class);
        assertFalse(creds.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals("You should be authorised", creds.getMessage(),
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
        UserCreds creds = response1.as(UserCreds.class);
        assertTrue(creds.isSuccess(),
                "ответ должен содержать success: true");
        assertNotNull(creds.getUser(),
                "В ответе должно быть поле 'user'");
        assertEquals(userRegister.getName(), creds.getUser().getName(),
                "Имя в ответе должно остаться прежним");
        assertNull(creds.getMessage(), "При успешном обновлении не должно быть сообщения об ошибке");

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
        UserCreds creds = response2.as(UserCreds.class);
        assertFalse(creds.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals("You should be authorised", creds.getMessage(),
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
        UserCreds creds = response3.as(UserCreds.class);
        assertTrue(creds.isSuccess(),
                "ответ должен содержать success: true");
        assertNotNull(creds.getUser(),
                "В ответе должно быть поле 'user'");
        assertEquals(userRegister.getEmail(), creds.getUser().getEmail(),
                "Email в ответе должен совпадать с отправленным");
        assertEquals(newName, creds.getUser().getName(),
                "Имя в ответе должно остаться прежним");
        assertNull(creds.getMessage(), "При успешном обновлении не должно быть сообщения об ошибке");
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
        UserCreds creds = response3.as(UserCreds.class);
        assertFalse(creds.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals("You should be authorised", creds.getMessage(),
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
