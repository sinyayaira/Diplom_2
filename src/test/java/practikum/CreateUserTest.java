package practikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends UserDependTest {

    @Test
    @DisplayName("Check create user")
    public void createUser() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Check create user without password")
    public void createUserWithoutPassword() {
        ClientPOJO clientWithoutPassword = new ClientPOJO(EMAIL, NAME, null);
        userClient.register(clientWithoutPassword)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check create user already exist")
    public void createUserWithLoginAlreadyExist() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        userClient.register(client)
            .statusCode(403)
            .body("success", equalTo(false))
            .body("message", equalTo("User already exists"));
    }
}

