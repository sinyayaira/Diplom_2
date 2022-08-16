package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UpdateUserTest extends UserDependTest {

    protected ClientPOJO clientForUpdate = new ClientPOJO(NEW_EMAIL, NEW_NAME, null);

    @Test
    @DisplayName("Check update user with authorization")
    public void updateUserWithAuthorization() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        Response response = userClient.login(client);

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        userClient.profile(token)
            .statusCode(200)
            .body("success", equalTo(true));

        userClient.update(clientForUpdate, token)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("user.email", equalTo(NEW_EMAIL))
            .body("user.name", equalTo(NEW_NAME));
    }

    @Test
    @DisplayName("Check update user without authorization")
    public void updateUserWithoutAuthorization() {
        userClient.update(clientForUpdate, null)
            .statusCode(401)
            .body("success", equalTo(false))
            .body("message", equalTo("You should be authorised"));
    }
}
