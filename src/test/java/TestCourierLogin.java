import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestCourierLogin extends CourierClient {

    private Courier courier;
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courier = CourierGenerator.randomCourier();
        courierClient = new CourierClient();

        courierClient.createCourier(courier);
    }

    @Test
    @DisplayName("курьер может авторизоваться")
    public void checkCourierCanLogin() {
        courierClient.loginCourier(courier).statusCode(SC_OK);
    }

    @Test
    @DisplayName("успешный запрос возвращает id")
    public void checkCourierLoginReturnsId() {
        courierClient.loginCourier(courier)
                .statusCode(SC_OK)
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void checkCourierLoginNotExistReturnsError() {
        courierClient.deleteCourier(courier).statusCode(SC_OK);

        courierClient.loginCourier(courier)
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("система вернёт ошибку, если неправильно указать логин")
    public void checkCourierLoginWrongLoginReturnsError() {
        String wrongLoginJson = String.format("{\"login\": \"%s\", \"password\": \"%s\"}",
                courier.getLogin() + (int) (Math.random() * 100), courier.getPassword());

        loginCustomCourier(wrongLoginJson)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("система вернёт ошибку, если неправильно указать пароль")
    public void checkCourierLoginWrongPassReturnsError() {
        String wrongPassJson = String.format("{\"login\": \"%s\", \"password\": \"%s\"}",
                courier.getLogin(), courier.getPassword() + (int) (Math.random() * 100));

        loginCustomCourier(wrongPassJson)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("для авторизации нужно передать все обязательные поля" +
            "если какого-то поля нет (login), запрос возвращает ошибку")
    public void checkCourierLoginWithoutLoginReturnsError() {
        String noLoginJson = String.format("{\"password\": \"%s\"}",
                courier.getPassword());

        loginCustomCourier(noLoginJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("если какого-то поля нет (login - пусто), запрос возвращает ошибку")
    public void checkCourierLoginEmptyLoginReturnsError() {
        String emptyLoginJson = String.format("{\"login\": \"\", \"password\": \"%s\"}",
                courier.getPassword());

        loginCustomCourier(emptyLoginJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("если какого-то поля нет (login - null), запрос возвращает ошибку")
    public void checkCourierLoginNullLoginReturnsError() {
        String nullLoginJson = String.format("{\"login\": %s, \"password\": \"%s\"}",
                null, courier.getPassword());

        loginCustomCourier(nullLoginJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("для авторизации нужно передать все обязательные поля" +
            "если какого-то поля нет (password), запрос возвращает ошибку")
    @Description("тут баг - неожиданное поведение системы (timeout), не обрабатывается кейс с отсутствием пароля")
    public void checkCourierLoginWithoutPassReturnsError() {
        String noPassJson = String.format("{\"login\": \"%s\"}",
                courier.getLogin());

        loginCustomCourier(noPassJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("если какого-то поля нет (password - пусто), запрос возвращает ошибку")
    public void checkCourierLoginEmptyPassReturnsError() {
        String emptyPassJson = String.format("{\"login\": \"%s\", \"password\": \"\"}",
                courier.getLogin());

        loginCustomCourier(emptyPassJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("если какого-то поля нет (password - null), запрос возвращает ошибку")
    public void checkCourierLoginNullPassReturnsError() {
        String nullPassJson = String.format("{\"login\": \"%s\", \"password\": %s}",
                courier.getLogin(), null);

        loginCustomCourier(nullPassJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("авторизация - пустой запрос")
    @Description("тут баг - неожиданное поведение системы (timeout)")
    public void checkCourierLoginEmptyBodyReturnsError() {
        String emptyJson = "";
        loginCustomCourier(emptyJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void cleanUp() {
        if (courierClient.loginCourier(courier).extract().statusCode() == SC_OK)
            courierClient.deleteCourier(courier).statusCode(SC_OK);
    }
}
