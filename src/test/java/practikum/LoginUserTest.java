package practikum;

import io.qameta.allure.junit4.DisplayName;
import net.minidev.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest extends UserDependTest {

    @Test
    @DisplayName("Check authorization")
    public void authorization() {
        JSONObject requestParamsOne = new JSONObject();
        requestParamsOne.put("email", EMAIL);
        requestParamsOne.put("password", PASSWORD);
        requestParamsOne.put("name", NAME);

        JSONObject requestParamsTwo = new JSONObject();
        requestParamsTwo.put("email", EMAIL);
        requestParamsTwo.put("password", PASSWORD);

        given()
                .header("Content-type", "application/json")
                .body(requestParamsOne.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        given()
                .header("Content-type", "application/json")
                .body(requestParamsTwo.toJSONString())
                .post("/api/auth/login")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Check authorization with wrong login")
    public void authorizationWithWrongLogin() {
        JSONObject requestParamsOne = new JSONObject();
        requestParamsOne.put("email", EMAIL);
        requestParamsOne.put("password", PASSWORD);
        requestParamsOne.put("name", NAME);

        JSONObject requestParamsTwo = new JSONObject();
        requestParamsTwo.put("email", "apple");
        requestParamsTwo.put("password", PASSWORD);

        given()
                .header("Content-type", "application/json")
                .body(requestParamsOne.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        given()
                .header("Content-type", "application/json")
                .body(requestParamsTwo.toJSONString())
                .post("/api/auth/login")
                .then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
