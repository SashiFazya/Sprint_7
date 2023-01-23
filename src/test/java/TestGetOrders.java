import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class TestGetOrders extends OrderClient {
    @Test
    public void checkGetOrdersReturnSth() {
        getOrdersList().then().statusCode(SC_OK).body(notNullValue());
    }
}

