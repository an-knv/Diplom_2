package Steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.UserRegister;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;


public class UserSteps {
    private static final String API_REGISTER = "/api/auth/register";
    private static final String API_USER = "/api/auth/user";
    private static final String API_LOGIN = "api/auth/login";

    public UserSteps() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
    }

    @Step("Создание пользователя")
    public Response registerUser(UserRegister userRegister) {

        return given()
                .contentType(JSON)
                .body(userRegister)
                .when()
                .post(API_REGISTER);
    }

    @Step("Вывести тело ответа в консоль")
    public void printResponseBody(Response response) {
        System.out.println("Тело ответа:");
        System.out.println(response.body().asString());
    }
    @Step("Извлечь accessToken из ответа")
    public String extractAccessTokenFromResponse(Response response) {

        return response.jsonPath().getString("accessToken");
    }
    @Step("Извлечь accessToken из ответа")
    public String extractRefreshTokenFromResponse(Response response) {

        return response.jsonPath().getString("refreshToken");
    }
    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .contentType(JSON)
                .header("Authorization", accessToken) // Bearer токен в заголовке
                .when()
                .delete(API_USER); // DELETE запрос на удаление пользователя
    }
    @Step("Авторизация пользователя")
    public Response loginUser(String accessToken, UserRegister userRegister) {

        return given()
                .contentType(JSON)
                .header("Authorization", accessToken)
                .body(userRegister)
                .when()
                .post(API_LOGIN);
    }

    @Step("обновление данных пользователя без авторизации")
    public Response updateUserWithoutAuth(UserRegister userRegister) {

        return  given()
                .contentType(JSON)
                .body(userRegister)
                .when()
                .patch(API_USER);
    }
    @Step("обновление данных пользователя c с авторизацией")
    public Response updateUser(String accessToken, UserRegister userRegister) {

        return  given()
                .contentType(JSON)
                .header("Authorization", accessToken)
                .body(userRegister)
                .when()
                .patch(API_USER);
    }
}