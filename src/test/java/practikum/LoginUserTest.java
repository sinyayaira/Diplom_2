package practikum;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest extends UserDependTest {

    protected ClientPOJO clientForLogin = new ClientPOJO(EMAIL, PASSWORD);

    @Test
    @DisplayName("Check authorization")
    public void authorization() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        userClient.login(clientForLogin)
            .then().assertThat()
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Check authorization with wrong login")
    public void authorizationWithWrongLogin() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        ClientPOJO wrongClientForLogin = new ClientPOJO("apple", PASSWORD);
        userClient.login(wrongClientForLogin)
            .then().assertThat()
            .statusCode(401)
            .body("success", equalTo(false))
            .body("message", equalTo("email or password are incorrect"));
    }
}
