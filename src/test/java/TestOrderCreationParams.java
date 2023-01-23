import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestOrderCreationParams extends OrderClient {

    List<Color> colors;
    private Order order;
    private int trackOrder;

    @Before
    public void setUp() {
        order = OrderGenerator.randomOrderNoColor().setColor(colors);
    }

    public TestOrderCreationParams(List<Color> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Color: {0}")
    public static Object[][] setOrderColor() {
        return new Object[][]{
                {List.of()},
                {List.of(ColorsEnum.BLACK)},
                {List.of(ColorsEnum.GREY)},
                {List.of(ColorsEnum.GREY, ColorsEnum.BLACK)}
        };
    }

    @Test
    @DisplayName("заказ можно создать")
    public void checkOrderCanBeCreated() {
        Response response = createOrder(order);
        response.then().statusCode(SC_CREATED);

        trackOrder = response.path("track");
    }

    @Test
    @DisplayName("тело ответа содержит track")
    public void checkOrderCreateReturnsTrack() {
        Response response = createOrder(order);
        response.then()
                .statusCode(SC_CREATED)
                .assertThat().body("track", notNullValue());

        trackOrder = response.path("track");
    }

    @After
    public void cleanUp() {
        cancelOrder(trackOrder);
    }
}
