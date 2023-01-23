import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String CREATE_ORDER_URL = "/api/v1/orders";
    private static final String CANCEL_ORDER_URL = "/api/v1/orders/cancel";
    private static final String GET_ORDERS_LIST = "/api/v1/orders";

    public Response createOrder(Order order) {

        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER_URL);
    }

    public Response cancelOrder(int track) {
        String cancelBody = String.format("{\"track\": \"%s\"}", track);

        return given()
                .spec(getSpec())
                .body(cancelBody)
                .when()
                .put(CANCEL_ORDER_URL);
    }

    public Response getOrdersList() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_ORDERS_LIST);
    }
}
