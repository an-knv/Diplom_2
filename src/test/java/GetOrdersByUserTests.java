import Steps.OrderSteps;
import Steps.UserSteps;
import generator.UserExample;
import io.restassured.response.Response;
import model.OrdersResponse;
import model.UserRegister;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
    }

    @Test
    @DisplayName("получение списка заказов без авторизации")
    public void getOrderWithoutAuth() {
        // Создание заказа для пользователя
        orderSteps.createOrderWithValidIngredients(accessToken);
        Response ordersResponse = orderSteps.getOrdersWithoutAuth();
        orderSteps.printResponseBody(ordersResponse);

        OrdersResponse ordersModel = ordersResponse.as(OrdersResponse.class);
        assertFalse(ordersModel.isSuccess(),
                "success должен быть false без авторизации");
        assertEquals("You should be authorised", ordersModel.getMessage(),
                "Должно быть сообщение об ошибке авторизации You should be authorised");
    }

    @Test
    @DisplayName("получение списка заказов c авторизацией")
    public void getOrderWithAuth() {
        Response createResponse = orderSteps.createOrderWithValidIngredients(accessToken);
        Integer createdOrderNumber = createResponse.jsonPath().getInt("order.number");
        String createdOrderId = createResponse.jsonPath().getString("order._id");
        String createdOrderName = createResponse.jsonPath().getString("name");
        List<String> createdIngredients = createResponse.jsonPath().getList("order.ingredients._id");

        System.out.println("Создан заказ " + createdOrderNumber +
                ", ID: " + createdOrderId +
                ", Название: " + createdOrderName);

        Response ordersResponse = orderSteps.getOrdersWithAuth(accessToken);
        orderSteps.printResponseBody(ordersResponse);

        assertEquals(SC_OK, ordersResponse.statusCode(),
                "должен возвращаться код 200");

        // Извлекаем список заказов
        List<Map<String, Object>> orders = ordersResponse.jsonPath().getList("orders");
        assertFalse(orders.isEmpty(), "Список заказов не должен быть пустым");

        Map<String, Object> foundOrder = orders.stream()
                .filter(order -> createdOrderId.equals(order.get("_id")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Созданный заказ не найден в списке"));

        assertEquals(createdOrderNumber, foundOrder.get("number"),
                "Номер заказа должен совпадать");
        assertEquals(createdOrderName, foundOrder.get("name"),
                "Название заказа должно совпадать");

        List<String> listIngredients = (List<String>) foundOrder.get("ingredients");
        assertEquals(createdIngredients, listIngredients,
                "Ингредиенты должны совпадать");

        assertNotNull(foundOrder.get("status"), "Статус заказа должен присутствовать");
        assertEquals("done", foundOrder.get("status"),
                "Статус заказа должен быть 'done'");
    }

    @Test
    @DisplayName("Проверка структуры списка заказов")
    public void verifyOrdersListStructure() {
        orderSteps.createOrderWithValidIngredients(accessToken);
        Response ordersResponse = orderSteps.getOrdersWithAuth(accessToken);
        OrdersResponse ordersModel = ordersResponse.as(OrdersResponse.class);

        assertTrue(ordersModel.isSuccess(),
                "success должно быть true");
        assertNotNull(ordersModel.getOrders(),
                "orders должно быть");
        assertNotNull(ordersModel.getTotal(),
                "total должно быть");
        assertNotNull(ordersModel.getTotalToday(),
                "totalToday должно быть");

        List<OrdersResponse.Order> orders = ordersModel.getOrders();
        assertFalse(orders.isEmpty(), "Список заказов не пустой");

        for (OrdersResponse.Order order : orders) {
            assertNotNull(order.get_id(), "Поле _id обязательно");
            assertNotNull(order.getNumber(), "Поле number обязательно");
            assertNotNull(order.getName(), "Поле name обязательно");
            assertNotNull(order.getStatus(), "Поле status обязательно");
            assertNotNull(order.getCreatedAt(), "Поле createdAt обязательно");
            assertNotNull(order.getUpdatedAt(), "Поле updatedAt обязательно");
            assertNotNull(order.getIngredients(), "Поле ingredients обязательно");
        }
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