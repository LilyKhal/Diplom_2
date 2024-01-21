import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static org.junit.Assert.assertEquals;
import static user.UserGenerator.randomUser;

public class CreateUserTest {
    UserClient userClient;
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URL;
        userClient = new UserClient();
    }
    @Test
    public void createUserTest() {
        User user = randomUser();

        Response createResponse = userClient.create(user);
        String token = createResponse.path(("accessToken"));

        assertEquals(true, createResponse.path("success"));

        userClient.delete(token);
    }
    @Test
    public void createExistedUserTest() {
        User user = randomUser();
        Response firstCreate = userClient.create(user);
        String token = firstCreate.path(("accessToken"));

        Response duplicateCreate = userClient.create(user);
        userClient.delete(token);

        assertEquals(403, duplicateCreate.statusCode());
        assertEquals(false, duplicateCreate.path("success"));
        assertEquals("User already exists", duplicateCreate.path("message"));
    }

    @Test
    public void createUserWithEmptyParameterTest(){
        User userWithoutEmail = new User("","98765","Sophie");

        Response userWithoutEmailResponse  = userClient.create(userWithoutEmail);
        assertEquals(403,userWithoutEmailResponse.statusCode());
        assertEquals("Email, password and name are required fields",userWithoutEmailResponse.path("message"));

        User userWithoutPassword = new User("GENRY12@yandex.ru","","Genry");

        Response userWithoutPasswordResponse  = userClient.create(userWithoutPassword);
        assertEquals(403,userWithoutPasswordResponse.statusCode());
        assertEquals("Email, password and name are required fields",userWithoutPasswordResponse.path("message"));

        User userWithoutName = new User("Anna312@yandex.ru","474939","");

        Response userWithoutNameResponse  = userClient.create(userWithoutName);
        assertEquals(403,userWithoutNameResponse.statusCode());
        assertEquals("Email, password and name are required fields",userWithoutNameResponse.path("message"));
    }

    }

