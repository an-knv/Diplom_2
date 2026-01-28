import generator.UserExample;
import model.UserRegister;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static generator.UserExample.randomUser;

public class LoginTests {


    private Steps.UserSteps steps;
    private UserRegister userRegister;
    private String accessToken;
    private Response response;

    @BeforeEach
    public void setUp() {
        steps = new Steps.UserSteps();
        UserExample userExample = UserExample.randomUser();
        userRegister = new UserRegister(userExample.getEmail(), userExample.getPassword(), userExample.getName());
        response = steps.registerUser(userRegister);
        steps.printResponseBody(response);
        accessToken = steps.extractAccessTokenFromResponse(response);
    }
    @Test
    @DisplayName("авторизация пользователя")

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
