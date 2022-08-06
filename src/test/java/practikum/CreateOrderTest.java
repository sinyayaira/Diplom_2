package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest extends UserDependTest {
    @Test
    @DisplayName("Check create order with wrong hash ingredients")
    public void createOrderWithWrongHashIngredients() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("password", PASSWORD);
        requestParams.put("name", NAME);

        JSONObject requestParamsTwo = new JSONObject();
        requestParamsTwo.put("ingredients","ыыыы61c0c5a71d1f82001bdaaa71");

        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/login");

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(requestParamsTwo.toJSONString())
                .post("/api/orders")
                .then().assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("Check create order without authorization")
    public void createOrderWithoutAuthorization() {
        JSONObject requestParamsTwo = new JSONObject();
        requestParamsTwo.put("ingredients", new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa70"});

        given()
                .header("Content-type", "application/json")
                .body(requestParamsTwo.toJSONString())
                .post("/api/orders")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Check create order with ingredients")
    public void createOrderWithIngredients() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("password", PASSWORD);
        requestParams.put("name", NAME);

        JSONObject requestParamsTwo = new JSONObject();
        requestParamsTwo.put("ingredients", new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa70"});

        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/login");

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(requestParamsTwo.toJSONString())
                .post("/api/orders")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Check create order without ingredients")
    public void createOrderWithOutIngredients() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("password", PASSWORD);
        requestParams.put("name", NAME);

        JSONObject requestParamsTwo = new JSONObject();
        requestParamsTwo.put("ingredients", new String[]{});

        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/login");

        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(requestParamsTwo.toJSONString())
                .post("/api/orders")
                .then().assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
}
