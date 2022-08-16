package practikum;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class UserClient extends BaseClient {
    public static final String USER_ENDPOINT = "/api/auth/user";
    public static final String REGISTER_ENDPOINT = "/api/auth/register";
    public static final String LOGIN_ENDPOINT = "/api/auth/login";

    public ValidatableResponse update(ClientPOJO client, String token) {
        return getRequestSpec(token)
            .body(client.toJSON())
            .patch(USER_ENDPOINT)
            .then().assertThat()
        ;
    }

    public ValidatableResponse profile(String token) {
        return getRequestSpec(token)
            .get(USER_ENDPOINT)
            .then().assertThat()
            ;
    }

    public ValidatableResponse delete(String token) {
        return getRequestSpec(token)
            .delete(USER_ENDPOINT)
            .then().assertThat()
            ;
    }

    public ValidatableResponse register(ClientPOJO client) {
        return getRequestSpec(null)
            .body(client.toJSON())
            .post(REGISTER_ENDPOINT)
            .then().assertThat()
            ;
    }

    public Response login(ClientPOJO client) {
        return getRequestSpec(null)
            .body(client.toJSON())
            .post(LOGIN_ENDPOINT)
            ;
    }
}
