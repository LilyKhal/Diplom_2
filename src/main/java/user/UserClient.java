package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import models.UserCred;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String CREATE_USER_URL = "/api/auth/register";
    private static final String LOGIN_USER_URL = "/api/auth/login";
    private static final String INFO_USER_URL = "/api/auth/user";


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
    @Step("Получение информации о пользователе {userCred}")
    public Response getInfo(String token) {
    return given()
            .header("Content-type","application/json")
            .and()
            .header("Authorization", token)
            .when()
            .get(INFO_USER_URL);
    }
    @Step("Изменение данных пользователя")
    public Response changeUserInfo(String token, User user) {
    return given()
            .header("Content-type","application/json")
            .and()
            .header("Authorization", token)
            .and()
            .body(user)
            .when()
            .patch(INFO_USER_URL);
    }

    @Step("Удаление пользователя по токену {token}")
    public Response delete(String token) {
        return given()
                .header("Content-type","application/json")
                .and()
                .header("Authorization", token)
                .when()
                .delete(INFO_USER_URL);
    }

}
