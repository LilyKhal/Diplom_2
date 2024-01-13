package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import models.UserCred;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String CREATE_USER_URL = "/api/auth/register";
    private static final String LOGIN_USER_URL = "/api/auth/login";
   // private static final String LOGOUT_USER_URL = "/api/auth/logout";

@Step("Создание пользователя {user}")
public Response create(User user){
    return given()
            .header("Content-type", "application/json")
            .and()
            .body(user)
            .when()
            .post(CREATE_USER_URL);
}
    @Step("Авторизация пользователя с кредами {userCred}")
    public Response login(UserCred userCred){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCred)
                .when()
                .post(LOGIN_USER_URL);
    }
}
