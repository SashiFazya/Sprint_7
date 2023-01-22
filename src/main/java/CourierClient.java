import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.*;

public class CourierClient extends Client {
    private static final String CREATE_COURIER_URL = "/api/v1/courier";
    private static final String LOGIN_COURIER_URL = "/api/v1/courier/login";
    private static final String DELETE_COURIER_URL = "api/v1/courier/{id}";

    public ValidatableResponse createCourier(Courier courier){

        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(CREATE_COURIER_URL)
                .then();
    }

    public ValidatableResponse createCustomCourier(String bodyJson){

        return given()
                .spec(getSpec())
                .body(bodyJson)
                .when()
                .post(CREATE_COURIER_URL)
                .then();
    }

    public ValidatableResponse loginCourier(Courier courier){
        LoginData loginData = new LoginData(courier);
        return given()
                .spec(getSpec())
                .body(loginData)
                .when()
                .post(LOGIN_COURIER_URL)
                .then();
    }

    public ValidatableResponse loginCustomCourier(String bodyJson){

        return given()
                .spec(getSpec())
                .body(bodyJson)
                .when()
                .post(LOGIN_COURIER_URL)
                .then();
    }

    public int getCourierId (Courier courier){
        LoginData loginData = new LoginData(courier);
        return given()
                .spec(getSpec())
                .body(loginData)
                .post(LOGIN_COURIER_URL)
                .path("id");
    }

    public ValidatableResponse deleteCourier (Courier courier){
        LoginData loginData = new LoginData(courier);
        return given()
                .spec(getSpec())
                .body(loginData)
                .pathParams("id", getCourierId(courier))
                .delete(DELETE_COURIER_URL)
                .then();
    }
}
