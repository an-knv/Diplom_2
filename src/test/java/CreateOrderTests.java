import steps.OrderSteps;
import steps.UserSteps;
import com.github.javafaker.Faker;
import generator.UserExample;
import io.restassured.response.Response;
import model.OrderCreater;
import model.OrderResponseCreator;
import model.UserRegister;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderTests {
    private UserSteps steps;
    private UserRegister userRegister;
    private String accessToken;
    private Response response;
    private final OrderSteps orderSteps = new OrderSteps();

    @BeforeEach
    public void setUp() {
        steps = new UserSteps();
        userRegister = UserExample.builder().build();
        response = steps.registerUser(userRegister);
        steps.printResponseBody(response);
        accessToken = steps.extractAccessTokenFromResponse(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    public void createOrderWithoutAuthWithIngredients() {

        Response response0 = orderSteps.getIngredients();
        System.out.println(orderSteps.extractBunAndMain(response0));
        List <String> ingredients = orderSteps.extractBunAndMain(response0);
        OrderCreater orderCreater = new OrderCreater(ingredients);
        Response response = orderSteps.createOrderWithoutAuth(orderCreater);
        orderSteps.printRequestBody(orderCreater);
        orderSteps.printResponseBody(response);
        //проверяем ответ
        assertEquals(SC_OK, response.statusCode(),
                "При успешном создании должен возвращаться код 200");
        OrderResponseCreator ordRes = response.as(OrderResponseCreator.class);
        assertTrue(ordRes.isSuccess(),
                "ответ должен содержать success: true");
        assertNotNull(ordRes.getOrder().getNumber(),
                "В ответе должен быть номер заказа");

    }
    @Test
    @DisplayName("Создание заказа с ингредиентами c авторизацией")
    public void createOrderWithAuthWithIngredients() {

        Response response0 = orderSteps.getIngredients();
        List <String> ingredients = orderSteps.extractBunAndMain(response0);
        OrderCreater orderCreater = new OrderCreater(ingredients);
        Response response = orderSteps.CreateOrderWithAuth(accessToken, orderCreater);
        orderSteps.printRequestBody(orderCreater);
        orderSteps.printResponseBody(response);
        //проверяем ответ
        assertEquals(SC_OK, response.statusCode(),
                "При успешном создании должен возвращаться код 200");
        OrderResponseCreator ordRes = response.as(OrderResponseCreator.class);
        assertTrue(ordRes.isSuccess(),
                "ответ должен содержать success: true");
        assertNotNull(ordRes.getOrder().getNumber(),
                "В ответе должен быть номер заказа");

    }
    @Test
    @DisplayName("Создание заказа c авторизацией без ингредиентов")
    public void createOrderWithAuthWithoutIngredients() {

        OrderCreater orderCreater = new OrderCreater(new ArrayList<>());
        Response response = orderSteps.CreateOrderWithAuth(accessToken, orderCreater);
        orderSteps.printRequestBody(orderCreater);
        orderSteps.printResponseBody(response);
        //проверяем ответ
        assertEquals(SC_BAD_REQUEST, response.statusCode(),
                "должен возвращаться код 400");
        OrderResponseCreator ordRes = response.as(OrderResponseCreator.class);
        assertFalse(ordRes.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals(ordRes.getMessage(),
                "Ingredient ids must be provided","Сообщение должно быть 'Ingredient ids must be provided'");

    }
    @Test
    @DisplayName("Создание заказа без авторизации без ингредиентов")
    public void createOrderWithAuthoutWithoutIngredients() {

        OrderCreater orderCreater = new OrderCreater(new ArrayList<>());
        Response response = orderSteps.createOrderWithoutAuth(orderCreater);
        orderSteps.printRequestBody(orderCreater);
        orderSteps.printResponseBody(response);
        assertEquals(SC_BAD_REQUEST, response.statusCode(),
                "должен возвращаться код 400");
        OrderResponseCreator ordRes = response.as(OrderResponseCreator.class);
        assertFalse(ordRes.isSuccess(),
                "ответ должен содержать success: false");
        assertEquals(ordRes.getMessage(),
                "Ingredient ids must be provided","Сообщение должно быть 'Ingredient ids must be provided'");

    }
    @Test
    @DisplayName("Создание заказа без авторизации и неверный хэш")
    public void createOrderWithAuthoutWrongIngredients() {

        Faker faker = new Faker();
        OrderCreater orderCreater = new OrderCreater(List.of(faker.crypto().md5(),faker.crypto().md5()));
        Response response = orderSteps.createOrderWithoutAuth(orderCreater);
        orderSteps.printRequestBody(orderCreater);
        orderSteps.printResponseBody(response);
        //проверяем ответ
        assertEquals(SC_INTERNAL_SERVER_ERROR, response.statusCode(),
                "должен возвращаться код 500");

    }
    @Test
    @DisplayName("Создание заказа с авторизацией и неверный хэш")
    public void createOrderWithAuthWrongIngredients() {

        Faker faker = new Faker();
        OrderCreater orderCreater = new OrderCreater(List.of(faker.crypto().md5(),faker.crypto().md5()));
        Response response = orderSteps.CreateOrderWithAuth(accessToken, orderCreater);
        orderSteps.printRequestBody(orderCreater);
        orderSteps.printResponseBody(response);
        assertEquals(SC_INTERNAL_SERVER_ERROR, response.statusCode(),
                "должен возвращаться код 500");

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
