import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.AvailableIngredients;
import models.OrderRequest;
import models.User;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static io.restassured.RestAssured.given;
import static models.UserCred.fromUser;
import static org.junit.Assert.assertEquals;
import static user.UserGenerator.randomUser;

public class CreateOrderTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    User user;
    UserClient userClient;
    String accessToken;
    OrderClient orderClient;
    private AvailableIngredients availableIngredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = randomUser();
        userClient = new UserClient();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken").toString();
        orderClient = new OrderClient();
        availableIngredients = given()
                                .header("Content-type", "application/json")
                                .get("https://stellarburgers.nomoreparties.site/api/ingredients")
                                .body().as(AvailableIngredients.class);
    }

    @Test
    public void createOrderWithAuthTest(){
        OrderRequest request = new OrderRequest();
        request.setIngredient(availableIngredients.getData().get(5).getId());
        Response createOrderWithAuthResponse = orderClient.createOrders(accessToken, request);
        assertEquals(true, createOrderWithAuthResponse.path("success"));
    }
    @Test
    public void createOrderWithoutAuthTest(){
        OrderRequest request = new OrderRequest();
        request.setIngredient(availableIngredients.getData().get(6).getId());
        Response createOrderWithoutAuthResponse = orderClient.createOrders("", request);
        assertEquals(true, createOrderWithoutAuthResponse.path("success"));
    }
    @Test
    public void createOrderWithInvalidHashTest(){
        OrderRequest request = new OrderRequest();
        request.setIngredient("7ffkygkyu");
        Response createOrderWithoutResponse = orderClient.createOrders("", request);
        assertEquals(500, createOrderWithoutResponse.statusCode());
    }
    @Test
    public void createOrderWithEmptyIngredTest(){
        OrderRequest request = new OrderRequest();
        Response createOrderWithoutResponse = orderClient.createOrders("", request);
        assertEquals(false, createOrderWithoutResponse.path("success"));
        assertEquals("Ingredient ids must be provided", createOrderWithoutResponse.path("message"));
    }
    @After
    public void deleteUser(){
        userClient.delete(accessToken);
    }
}
