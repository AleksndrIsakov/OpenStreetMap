package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OSMRestClient extends OSMConfigClient {

    private static final String PERMISSIONS = "permissions";
    private static final String USER_PREFERENCES = "user/preferences";

    @Step("Get permissions")
    public ValidatableResponse getPermissions() {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .get(PERMISSIONS)
                .then();

        return response;
    }

    @Step("Get preferences")
    public ValidatableResponse getPreferences() {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .get(USER_PREFERENCES)
                .then();

        return response;
    }

    @Step("Add preferences")
    public ValidatableResponse addPreferences(String body) {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .put(USER_PREFERENCES)
                .then();

        return response;
    }

    @Step("Delete preferences")
    public ValidatableResponse deletePreferences(String keyName) {
        ValidatableResponse response = given()
                .spec(getBaseSpec())
                .when()
                .delete(USER_PREFERENCES + "/" + keyName)
                .then();

        return response;
    }

}
