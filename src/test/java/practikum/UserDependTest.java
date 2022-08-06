package practikum;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;

public class UserDependTest {
    public static final String EMAIL = "burburiska99999@gmail.com";
    public static final String PASSWORD = "111111";
    public static final String NAME = "vasily";
    public static final String NEW_EMAIL = "mail"+ System.currentTimeMillis() + "@gmail.com";
    public static final String NEW_NAME = "name"+ System.currentTimeMillis();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        cleanup();
    }

    @After
    public void cleanUpEach() {
        cleanup();
    }

    public static void cleanup() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", EMAIL);
        requestParams.put("password", PASSWORD);
        requestParams.put("name", NAME);

        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .body(requestParams.toJSONString())
                .post("/api/auth/login");


        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("accessToken");
        if (token != null) {
            RestAssured.given()
                    .header("Authorization", token)
                    .when()
                    .delete("/api/auth/user");
        }
    }
 }

