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

public class GetUserOrdersTest extends UserDependTest{

    @Test
    @DisplayName("Check get user orders with authorization")
    public void updateUserWithAuthorization() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("password", PASSWORD);
        requestParams.put("name", NAME);

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
                .body(requestParams.toJSONString())
                .post("/api/auth/login")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(requestParams.toJSONString())
                .get("/api/orders")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("total", notNullValue());

    }

    @Test
    @DisplayName("Check get user orders without authorization")
    public void updateUserWithoutAuthorization() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("password", PASSWORD);
        requestParams.put("name", NAME);

        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .get("/api/orders")
                .then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
