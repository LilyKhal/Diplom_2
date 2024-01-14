package user;

import models.User;

import static utils.Utils.randomString;

public class UserGenerator {

        public static User randomUser() {
            return new User(String.format("%s@yandex.ru",randomString(8)), randomString(8), randomString(11));
        }
}

