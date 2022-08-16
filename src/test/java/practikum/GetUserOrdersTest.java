package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrdersTest extends UserDependTest{

    @Test
    @DisplayName("Check get user orders with authorization")
    public void updateUserWithAuthorization() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        Response response = userClient.login(client);

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        orderClient.list(token)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("total", notNullValue());
    }

    @Test
    @DisplayName("Check get user orders without authorization")
    public void updateUserWithoutAuthorization() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        orderClient.list(null)
            .statusCode(401)
            .body("success", equalTo(false))
            .body("message", equalTo("You should be authorised"));
    }
}
