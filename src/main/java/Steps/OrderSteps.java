package Steps;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.OrderCreater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    private static final String CREATE_ORDER = "/api/orders";
    private static final String GET_INGREDIENTS = "/api/ingredients";
    private static final String ORDERS = "/api/orders";
    public OrderSteps() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
    }


    @Step("Создание заказа без авторизации")
    public Response CreateOrderWithoutAuth(OrderCreater orderCreater) {

        return  given()
                .header("Content-Type", "application/json")
                .body(orderCreater)
                .when()
                .post(CREATE_ORDER );
    }
    @Step("Создание заказа c авторизацией")
    public Response CreateOrderWithAuth(String accessToken, OrderCreater orderCreater) {

        return  given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(orderCreater)
                .when()
                .post(CREATE_ORDER );
    }
    @Step("Вывести тело ответа в консоль")
    public void printResponseBody(Response response) {
        System.out.println("Тело ответа:");
        System.out.println(response.body().asString());
    }
    @Step("получаем список ингредиентов")
    public Response getIngredients() {

        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(GET_INGREDIENTS);
        }
    @Step("извлекаем список ингредиентов")
    public List<String> extractIngredient(Response response, String type) {
        List<String> ingredientIds = new ArrayList<>();
            List<Map<String, Object>> ingredients = response.path("data");
        for (Map<String, Object> ingredient : ingredients) {
            String ingredientType = (String) ingredient.get("type");
            if (type.equals(ingredientType)) {
                String id = (String) ingredient.get("_id");
                ingredientIds.add(id);
            }
        }

        return ingredientIds;
    }
    @Step("Извлекаем ID булки и начинки для создания заказа")
    public List <String> extractBunAndMain(Response response) {
        List<String> bunIds = extractIngredient(response, "bun");
        List<String> mainIds = extractIngredient(response, "main");
        // Выбираем первую булку и первую начинку
        String bunId = bunIds.get(0);
        String mainId = mainIds.get(0);

        return List.of(bunId, mainId);
    }
    @Step("Печать тела запроса")
    public void printRequestBody(Object requestBody) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(requestBody);
        System.out.println(jsonBody);
    }
    @Step("получаем список заказов пользователя c авторизацией")
    public Response getOrdersWithAuth(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS);
    }
    @Step("получаем список заказов пользователя без авторизации")
    public Response getOrdersWithoutAuth() {

        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(ORDERS);
    }
    @Step("Создание заказа с авторизацией и валидными ингредиентами")
    public Response createOrderWithValidIngredients(String accessToken) {
        Response ingredientsResponse = getIngredients();
        List<String> ingredients = extractBunAndMain(ingredientsResponse);
        OrderCreater orderCreater = new OrderCreater(ingredients);
        Response orderResponse = CreateOrderWithAuth(accessToken, orderCreater);
        return orderResponse;
    }
    }

