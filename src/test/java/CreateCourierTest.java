import io.qameta.allure.Attachment;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.Courier;
import ru.yandex.praktikum.dto.CourierRequest;
import ru.yandex.praktikum.dto.LoginRequest;
import ru.yandex.praktikum.generator.LoginRequestGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.yandex.praktikum.generator.CourierRequestGenerator.*;

public class CreateCourierTest {

    private Courier courierClient;
    private Integer id;

    @Before
    public void setUp() {
        courierClient = new Courier();
    }

    @After
    public void tearDown() {
        if (id != null) {
            courierClient.delete(id)
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }

    @Attachment(value = "Заголовок вложения", type = "text/html")
    public ValidatableResponse sendMessageToReport(ValidatableResponse message) {
        return message;
    }

    @Test
    @DisplayName("Verification of courier creation(Создание нового курьера))")
    public void creatingNewСourier() {
        CourierRequest courierRequest = getRandomCourier();
        courierClient.create(courierRequest).assertThat().statusCode(SC_CREATED)
                .and().body("ok", equalTo(true));
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
        System.out.println(id);
    }

    @Test
    @DisplayName("API POST /api/v1/courier Recreating a courier (Повторное создание курьера))")
    public void creatingDuplicatingCourier() {
        CourierRequest courierRequest = getRandomCourier();
        courierClient.create(courierRequest).assertThat().statusCode(SC_CREATED)
                .and().body("ok", equalTo(true));
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
        System.out.println(id);
        courierClient.create(courierRequest).assertThat().statusCode(SC_CONFLICT);
    }

    @Test
    @DisplayName("Сreating a courier without a name(Создание курьера без имени)")
    public void сreatingСourierWithoutName() {
        CourierRequest courierRequest = getCourierWithoutFirstName();
        courierClient.create(courierRequest).assertThat().statusCode(SC_CREATED)
                .and().body("ok", equalTo(true));
        LoginRequest loginRequest = LoginRequestGenerator.from(courierRequest);
        id = courierClient.login(loginRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract()
                .path("id");
        System.out.println(id);

    }

    @Test
    @DisplayName("Creating a courier without a password (создание курьера без пароля)")
    public void сreatingСourierWithoutPassword() {
        CourierRequest courierRequest = getCourierWithoutPassword();
        courierClient.create(courierRequest).assertThat().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Сreating a courier without a login (создание курьера без логина)")
    public void сreatingСourierWithoutLogin() {
        CourierRequest courierRequest = getCourierWithoutLogin();
        courierClient.create(courierRequest).assertThat().statusCode(SC_BAD_REQUEST);
    }
}
