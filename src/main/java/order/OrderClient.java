package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_URL = "/api/orders";
    @Step("Создание заказа")
    public Response createOrders(String token, OrderRequest request) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .and()
                .body(request)
                .when()
                .post(ORDER_URL);
    }
    @Step("Получение заказов конкретного ползователя")
    public Response getUserOrders(String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .when()
                .get(ORDER_URL);
    }
}
