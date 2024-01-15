import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;

import static models.UserCred.fromUser;
import static org.junit.Assert.assertEquals;
import static user.UserGenerator.randomUser;

public class UserInfoTest {
    User user;
    UserClient userClient;
    String accessToken;
    String refreshToken;
    Gson gson = new Gson();
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = randomUser();
        userClient = new UserClient();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken").toString();
        refreshToken = response.path("refreshToken").toString();
    }
    @Test
    public void getUserInfoTest() {
        Response userInfoResponse = userClient.getInfo(accessToken);
        assertEquals(true, userInfoResponse.path("success"));
        User actualUser = gson.fromJson(userInfoResponse.path("user").toString(), User.class);
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getName(), actualUser.getName());
    }
    @Test
    public void changeUserInfoWithoutAuthorizationTest() {
        Response changeUserInfoWithoutAuthResponse = userClient.changeUserInfo("", user);
        assertEquals(false, changeUserInfoWithoutAuthResponse.path("success"));
        assertEquals("You should be authorised", changeUserInfoWithoutAuthResponse.path("message"));
        assertEquals(401, changeUserInfoWithoutAuthResponse.statusCode());
    }
    @Test
    public void changeUserInfoWithAuthorizationTest() {
        //меняем данные у переменной user
        User randomUser = randomUser();
        user.setName(randomUser.getName());
        user.setEmail(randomUser.getEmail());
        user.setPassword(randomUser.getPassword());
        //запрос на изменение данных у user
        Response changeResponse = userClient.changeUserInfo(accessToken, user);
        assertEquals(true, changeResponse.path("success"));
        User actualUser = gson.fromJson(changeResponse.path("user").toString(), User.class);
        //проверка, что данные почты и имени изменились
        assertEquals(user.getName(), actualUser.getName());
        assertEquals(user.getEmail(), actualUser.getEmail());
        //проверка изменения пароля(с помощью авторизации с новым паролем)
        Response response = userClient.login(fromUser(user));
        assertEquals(200, response.statusCode());
        assertEquals(true, response.path("success"));
    }
    @Test
    public void setExistingEmailTest(){
        User randomUser = randomUser();
        userClient.create(randomUser);
        String randomEmail = randomUser.getEmail();

        user.setEmail(randomEmail);
        Response ExistingEmailResponse = userClient.changeUserInfo(accessToken, user);
        assertEquals(false, ExistingEmailResponse.path("success"));
        assertEquals("User with such email already exists", ExistingEmailResponse.path("message"));
    }
    @After
    public void deleteUser(){
        userClient.delete(accessToken);
    }
}
