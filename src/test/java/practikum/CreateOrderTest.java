package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends UserDependTest {
    @Test
    @DisplayName("Check create order with wrong hash ingredients")
    public void createOrderWithWrongHashIngredients() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        Response response = userClient.login(client);

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        orderClient.create(List.of("ыыыы61c0c5a71d1f82001bdaaa71"), token)
            .statusCode(500);
    }

    @Test
    @DisplayName("Check create order without authorization")
    public void createOrderWithoutAuthorization() {
        orderClient.create(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa70"), null)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Check create order with ingredients")
    public void createOrderWithIngredients() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        Response response = userClient.login(client);

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        orderClient.create(List.of("61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa70"), token)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Check create order without ingredients")
    public void createOrderWithOutIngredients() {
        userClient.register(client)
            .statusCode(200)
            .body("success", equalTo(true))
            .body("accessToken", notNullValue());

        Response response = userClient.login(client);

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        orderClient.create(List.of(), token)
            .statusCode(400)
            .body("success", equalTo(false))
            .body("message", equalTo("Ingredient ids must be provided"));
    }
}
