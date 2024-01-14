import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import models.UserCred;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static models.UserCred.fromUser;
import static org.junit.Assert.assertEquals;
import static user.UserGenerator.randomUser;

public class LoginUserTest {
    User user;
    String token;
    UserClient userClient;
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URL;
        user = randomUser();
        userClient = new UserClient();
        Response createResponse = userClient.create(user);
        token = createResponse.path(("accessToken"));
    }
    @Test
    public void loginUserTest() {
        Response response = userClient.login(fromUser(user));
        assertEquals(200, response.statusCode());
        assertEquals(true, response.path("success"));
    }
    @Test
    public void loginWithWrongCredsTest(){
        Response wrongLoginResponse = userClient.login(new UserCred(user.getEmail() + "a", user.getPassword()));
        assertEquals(401, wrongLoginResponse.statusCode());
        assertEquals(false, wrongLoginResponse.path("success"));

        Response wrongPasswordResponse = userClient.login(new UserCred(user.getEmail(), user.getPassword() + "2"));
        assertEquals(401, wrongPasswordResponse.statusCode());
        assertEquals("email or password are incorrect", wrongPasswordResponse.path("message"));

    }
    @Test
    public void loginWithoutSomeCredsTest(){
        Response withoutEmailResponse = userClient.login(new UserCred( "", user.getPassword()));
        assertEquals(401, withoutEmailResponse.statusCode());
        assertEquals(false, withoutEmailResponse.path("success"));

        Response withoutPasswordResponse = userClient.login(new UserCred(user.getEmail(), ""));
        assertEquals(401, withoutPasswordResponse.statusCode());
        assertEquals(false, withoutPasswordResponse.path("success"));
    }

    @After
    public void deleteUser(){
         userClient.delete(token);
    }
}


