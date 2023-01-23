import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class TestCourierCreation extends CourierClient {
    private Courier courier;
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courier = CourierGenerator.randomCourier();
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("курьера можно создать + запрос возвращает правильный код ответа")
    public void checkIfAbleCreateCourier() {
        courierClient.createCourier(courier).statusCode(SC_CREATED);
        courierClient.loginCourier(courier).statusCode(SC_OK);
    }

    @Test
    @DisplayName("успешный запрос возвращает ok: true")
    public void checkSuccessCreateCourier() {
        courierClient.createCourier(courier).assertThat().body("ok", equalTo(true));
        courierClient.loginCourier(courier).statusCode(SC_OK);
    }

    @Test
    @DisplayName("нельзя создать двух одинаковых курьеров" +
            "если создать пользователя с логином, который уже есть, возвращается ошибка - проверка кода ошибки")
    public void checkCodeImpossibleToCreateTheSameCourier() {
        courierClient.createCourier(courier).statusCode(SC_CREATED);
        courierClient.loginCourier(courier).statusCode(SC_OK);

        courierClient.createCourier(courier).statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("если создать пользователя с логином, который уже есть, возвращается ошибка - проверка текста ошибки")
    @Description("тут баг - текст ошибки не соответствует спецификации")
    public void checkTextImpossibleToCreateTheSameCourier() {
        courierClient.createCourier(courier).statusCode(SC_CREATED);
        courierClient.loginCourier(courier).statusCode(SC_OK);

        courierClient.createCourier(courier)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля" +
            "если одного из полей нет (login), запрос возвращает ошибку")
    public void checkIfLoginAbsentCreateCourier() {
        String noLoginJson = String.format("{\"password\": \"%s\", \"firstName\": \"%s\"}",
                courier.getPassword(), courier.getFirstName());

        createCustomCourier(noLoginJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет (login - пусто), запрос возвращает ошибку")
    public void checkIfLoginEmptyCreateCourier() {
        String noLoginJson = String.format("{\"login\": \"\", \"password\": \"%s\", \"firstName\": \"%s\"}",
                courier.getPassword(), courier.getFirstName());

        createCustomCourier(noLoginJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет (login - null), запрос возвращает ошибку")
    public void checkIfLoginNullCreateCourier() {
        String noLoginJson = String.format("{\"login\": %s, \"password\": \"%s\", \"firstName\": \"%s\"}",
                null, courier.getPassword(), courier.getFirstName());

        createCustomCourier(noLoginJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля" +
            "если одного из полей нет(password), запрос возвращает ошибку")
    public void checkIfPasswordAbsentCreateCourier() {
        String noPasswordJson = String.format("{\"login\": \"%s\", \"firstName\": \"%s\"}",
                courier.getLogin(), courier.getFirstName());

        createCustomCourier(noPasswordJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет(password - пусто), запрос возвращает ошибку")
    public void checkIfPasswordEmptyCreateCourier() {
        String noPasswordJson = String.format("{\"login\": \"%s\", \"password\": \"\", \"firstName\": \"%s\"}",
                courier.getLogin(), courier.getFirstName());

        createCustomCourier(noPasswordJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет(password - null), запрос возвращает ошибку")
    public void checkIfPasswordNullCreateCourier() {
        String noPasswordJson = String.format("{\"login\": \"%s\", \"password\": %s, \"firstName\": \"%s\"}",
                courier.getLogin(), null, courier.getFirstName());

        createCustomCourier(noPasswordJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля" +
            "если одного из полей нет (firstName), запрос возвращает ошибку")
    @Description("тут баг - по спецификации поле firstName является обязательным, однако создать курьера можно без него")
    public void checkIfFirstNameAbsentCreateCourier() {
        String noFirstNameJson = String.format("{\"login\": \"%s\", \"password\": \"%s\"}",
                courier.getLogin(), courier.getPassword());

        createCustomCourier(noFirstNameJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет (firstName - пусто), запрос возвращает ошибку")
    @Description("тут баг - по спецификации поле firstName является обязательным, однако создать курьера можно без него")
    public void checkIfFirstNameEmptyCreateCourier() {
        String noFirstNameJson = String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"\"}",
                courier.getLogin(), courier.getPassword());

        createCustomCourier(noFirstNameJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("если одного из полей нет (firstName - null), запрос возвращает ошибку")
    @Description("тут баг - по спецификации поле firstName является обязательным, однако создать курьера можно без него")
    public void checkIfFirstNameNullCreateCourier() {
        String noFirstNameJson = String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": %s}",
                courier.getLogin(), courier.getPassword(), null);

        createCustomCourier(noFirstNameJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("чтобы создать курьера, нужно передать в ручку все обязательные поля - пустой запрос")
    public void checkIfNoFieldsToCreateCourier() {
        String emptyJson = "";

        createCustomCourier(emptyJson)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void cleanUp() {
        if (courierClient.loginCourier(courier).extract().statusCode() == SC_OK)
            courierClient.deleteCourier(courier).statusCode(SC_OK);
    }
}