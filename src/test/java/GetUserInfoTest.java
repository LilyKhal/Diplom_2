import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserCred;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static org.junit.Assert.assertEquals;
import static user.UserGenerator.randomUser;

public class GetUserInfoTest {
    User user;
    UserClient userClient;
    String createToken;
    Gson gson = new Gson();
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = randomUser();
        userClient = new UserClient();
        Response response = userClient.create(user);
        createToken = response.path("accessToken").toString();
    }
    @Test
    public void getUserInfoTest() {
        Response userInfoResponse = userClient.getInfo(createToken);
        assertEquals(true, userInfoResponse.path("success"));
        User actualUser = gson.fromJson(userInfoResponse.path("user").toString(), User.class);
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getName(), actualUser.getName());
    }
    @Test
    public void getUserInfoWithoutAuthorizationTest() {
        Response userInfoWithoutAuthResponse = userClient.getInfo(createToken);
        assertEquals("You should be authorised", userInfoWithoutAuthResponse.path("message"));
        assertEquals(401, userInfoWithoutAuthResponse.statusCode());
    }
}
