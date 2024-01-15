import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static user.UserGenerator.randomUser;

public class GetUserOrdersTest {
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

        OrderRequest request1 = new OrderRequest();
        request1.setIngredient(availableIngredients.getData().get(5).getId());
        orderClient.createOrders(accessToken, request1);

        OrderRequest request2 = new OrderRequest();
        request2.setIngredient(availableIngredients.getData().get(6).getId());
        request2.setIngredient(availableIngredients.getData().get(2).getId());
        orderClient.createOrders(accessToken, request2);
    }
    @Test
    public void getUserOrdersWithoutAuthTest(){
        Response getUserOrdersWithoutAuthResponse = orderClient.getUserOrders("");
        assertEquals(false, getUserOrdersWithoutAuthResponse.path("success"));
        assertEquals("You should be authorised", getUserOrdersWithoutAuthResponse.path("message"));
    }
    @Test
    public void getUserOrdersWithAuthTest(){
        Response getUserOrdersWithAuthResponse = orderClient.getUserOrders(accessToken);
        assertEquals(true, getUserOrdersWithAuthResponse.path("success"));

        UserOrders userOrders = getUserOrdersWithAuthResponse.body().as(UserOrders.class);
        assertEquals(2, userOrders.getOrders().size());

        List<String> firstOrderIngredients = userOrders.getOrders().get(0).getIngredients();
        assertEquals(1, firstOrderIngredients.size());
        assertEquals(availableIngredients.getData().get(5).getId(), firstOrderIngredients.get(0));

        List<String> secondOrderIngredients = userOrders.getOrders().get(1).getIngredients();
        assertEquals(2, secondOrderIngredients.size());
        assertEquals(availableIngredients.getData().get(6).getId(), secondOrderIngredients.get(0));
        assertEquals(availableIngredients.getData().get(2).getId(), secondOrderIngredients.get(1));
    }
    @After
    public void deleteUser(){
        userClient.delete(accessToken);
    }
}
