import org.assertj.core.api.SoftAssertions;
import steps.OrderSteps;
import steps.UserSteps;
import generator.UserExample;
import io.restassured.response.Response;
import model.GetOrdersResponse;
import model.OrderResponseCreator;
import model.UserRegister;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.*;

public class GetOrdersByUserTests {
    private UserSteps steps;
    private UserRegister userRegister;
    private String accessToken;
    private Response response;
    private OrderSteps orderSteps;

    @BeforeEach
    public void setUp() {
        steps = new UserSteps();
        orderSteps = new OrderSteps();
        userRegister = UserExample.builder().build();
        response = steps.registerUser(userRegister);
        steps.printResponseBody(response);
        accessToken = steps.extractAccessTokenFromResponse(response);
        //System.out.println(accessToken);
    }

    @Test
    @DisplayName("получение списка заказов без авторизации")
    public void getOrderWithoutAuth() {
        // Создание заказа для пользователя
        orderSteps.createOrderWithValidIngredients(accessToken);
        Response ordersResponse = orderSteps.getOrdersWithoutAuth();
        orderSteps.printResponseBody(ordersResponse);

        GetOrdersResponse ordersModel = ordersResponse.as(GetOrdersResponse.class);
        assertFalse(ordersModel.isSuccess(),
                "success должен быть false без авторизации");
        assertEquals("You should be authorised", ordersModel.getMessage(),
                "Должно быть сообщение об ошибке авторизации You should be authorised");
    }

    @Test
    @DisplayName("получение списка заказов c авторизацией")
    public void getOrderWithAuth() {
        Response createResponse = orderSteps.createOrderWithValidIngredients(accessToken);
        OrderResponseCreator ordersModel = createResponse.as(OrderResponseCreator.class);

        Integer createdOrderNumber = ordersModel.getOrder().getNumber();
        String createdOrderId = ordersModel.getOrder().get_id();
        String createdOrderName = ordersModel.getOrder().getName();

        System.out.println("Создан заказ " + createdOrderNumber +
                ", ID: " + createdOrderId +
                ", Название: " + createdOrderName);

        Response getOrdersResponse = orderSteps.getOrdersWithAuth(accessToken);
        orderSteps.printResponseBody(getOrdersResponse);

        assertEquals(SC_OK, getOrdersResponse.statusCode(),
                "должен возвращаться код 200");

        // Извлекаем список заказов
        GetOrdersResponse foundOrder = getOrdersResponse.as(GetOrdersResponse.class);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(foundOrder.getOrders()).isNotEmpty();
        softly.assertThat(foundOrder.getOrders().get(0).getNumber()).isEqualTo(createdOrderNumber);
        softly.assertThat(foundOrder.getOrders().get(0).getName()).isEqualTo(createdOrderName);
        softly.assertThat(foundOrder.getOrders().get(0).getStatus()).isEqualTo("done");
        softly.assertAll();
        
    }

    @Test
    @DisplayName("Проверка структуры списка заказов")
    public void verifyOrdersListStructure() {
        orderSteps.createOrderWithValidIngredients(accessToken);
        Response ordersResponse = orderSteps.getOrdersWithAuth(accessToken);
        GetOrdersResponse ordersModel = ordersResponse.as(GetOrdersResponse.class);

        assertTrue(ordersModel.isSuccess(),
                "success должно быть true");
        assertNotNull(ordersModel.getOrders(),
                "orders должно быть");
        assertNotNull(ordersModel.getTotal(),
                "total должно быть");
        assertNotNull(ordersModel.getTotalToday(),
                "totalToday должно быть");

            assertNotNull(ordersModel.getOrders().get(0).get_id(), "Поле _id обязательно");
            assertNotNull(ordersModel.getOrders().get(0).getNumber(), "Поле number обязательно");
            assertNotNull(ordersModel.getOrders().get(0).getName(), "Поле name обязательно");
            assertNotNull(ordersModel.getOrders().get(0).getStatus(), "Поле status обязательно");
            assertNotNull(ordersModel.getOrders().get(0).getCreatedAt(), "Поле createdAt обязательно");
            assertNotNull(ordersModel.getOrders().get(0).getUpdatedAt(), "Поле updatedAt обязательно");
            assertNotNull(ordersModel.getOrders().get(0).getIngredients(), "Поле ingredients обязательно");
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