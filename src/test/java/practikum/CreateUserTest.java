package practikum;

import io.qameta.allure.junit4.DisplayName;
import net.minidev.json.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends UserDependTest {

    @Test
    @DisplayName("Check create user")
    public void createUser() {
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
    }

    @Test
    @DisplayName("Check create user without password")
    public void createUserWithoutPassword() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("name", NAME);
        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check create user already exist")
    public void createUserWithLoginAlreadyExist() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("password", PASSWORD);
        requestParams.put("name", NAME);

        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("api/auth/register")
                .then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());


        given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/register")
                .then().assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));

    }
}

